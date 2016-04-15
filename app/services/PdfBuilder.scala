/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

package services

import java.io.File
import java.util.Locale

import com.typesafe.scalalogging.LazyLogging
import controllers.ProgramItem
import models.{Lecture, WorkShopItem}
import org.apache.pdfbox.pdmodel.font.PDType1Font
import org.apache.pdfbox.pdmodel.{PDDocument, PDPage, PDPageContentStream}
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

import scala.collection.mutable.ArrayBuffer
import scala.util.Try
import scala.util.control.NonFatal

class Cursor(document: PDDocument) extends LazyLogging {

  private var x: Float = _
  private var y: Float = _

  private var page: PDPage = null

  private val bottom: Float = 20
  private val left: Float = 93
  private val right: Float = 30
  private val top: Float = 90

  def this(document: PDDocument, x: Float, y: Float) {
    this(document)
    if (document.getPages.getCount == 0) {
      page = new PDPage()
      document.addPage(page)
    } else {
      page = document.getPage(0)
    }

    this.x = x
    this.y = y

    logger.debug(s" Create new cursor with positions ($x, $y)")
  }

  /**
    * Split text into lines according to page width
    *
    * @return list of lines
    */
  private def textToLines(txt: String, font: PDType1Font, fontSize: Float): List[String] = {
    var text = txt
    var lines = new ArrayBuffer[String]()

    val width = page.getMediaBox.getWidth - left - right
    var lastSpace = -1

    while (text.length > 0) {
      var spaceIndex = text.indexOf(' ', lastSpace + 1)
      if (spaceIndex < 0) spaceIndex = text.length()

      var subString = text.substring(0, spaceIndex)
      val size = fontSize * font.getStringWidth(subString) / 1000

      if (size > width) {
        if (lastSpace < 0) lastSpace = spaceIndex
        subString = text.substring(0, lastSpace)
        lines.+=(subString)
        text = text.substring(lastSpace).trim()
        lastSpace = -1
      } else if (spaceIndex == text.length()) {
        lines.+=(text)
        text = ""
      } else {
        lastSpace = spaceIndex
      }
    }

    lines = lines.filter(_.length > 0)

    logger.debug(s" Text to write is '$txt'. Total lines ${lines.length}.")

    lines.toList
  }

  private def getTextHeight(lines: List[String], font: PDType1Font, size: Float): Float = {
    lines.length * font.getFontDescriptor.getFontBoundingBox.getHeight / 1000 * size
  }

  def withStream(block: PDPageContentStream => Unit): Unit = {
    var stream: PDPageContentStream = null
    try {
      stream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true)
      stream.setNonStrokingColor(0, 0, 0)
      stream.beginText()
      stream.newLineAtOffset(x, y)

      block(stream)

      stream.endText()
    } finally {
      stream.close()
    }
  }

  private def writeLines(lines: List[String], font: PDType1Font, size: Float): Unit = {
    withStream { stream =>
      stream.setFont(font, size)
      for (line <- lines) {
        logger.debug(s" Started write lines at position ($x, $y)")
        stream.showText(line)

        // Update the y position of the cursor after the text was written into a stream
        y -= getTextHeight(List(line), font, size) + 1.2f * size
        logger.debug(s" Update position after write new line ($x, $y)")
        stream.newLine()
        stream.newLineAtOffset(0, -1.5f * size)
      }
    }
  }

  def write(text: String, font: PDType1Font, size: Float): Unit = {
    val lines = textToLines(text, font, size)

    var height = 0.0f
    val (linesOnCurrentPage, linesOnNextPage) = lines.partition { line =>
      height = height + getTextHeight(List(line), font, size) + 1.2f * size
      y - height > bottom
    }

    // Write current lines
    writeLines(linesOnCurrentPage, font, size)

    // Add new page when current lines was written
    if (linesOnNextPage.nonEmpty) {
      page = new PDPage(page.getMediaBox)
      document.addPage(page)
      x = left
      y = page.getMediaBox.getUpperRightY - top
      writeLines(linesOnNextPage, font, size)
    }
  }

}

class ProgramCursor(document: PDDocument, x: Float, y: Float) extends Cursor(document, x, y) {

  val DATE_FORMATTER = "EEEEE, d MMMMM"

  val WORKSHOP_DATE_FORMATTER = "HH : mm"

  def writeDate(dateTime: DateTime): Unit = {
    val text = dateTime.toString(DATE_FORMATTER, Locale.US)
    write(text, PDType1Font.HELVETICA_BOLD, 14)
  }

  def writeWorkShopItem(item: WorkShopItem): Unit = {
    val text = s"${item.title} ${item.startDate.toString(WORKSHOP_DATE_FORMATTER, Locale.US)}" +
      s" ${item.endDate.toString(WORKSHOP_DATE_FORMATTER, Locale.US)}"
    write(text, PDType1Font.HELVETICA_BOLD, 13)
  }

  def writeLecture(lecture: Lecture): Unit = {
    val date = s"${lecture.date.toString(WORKSHOP_DATE_FORMATTER, Locale.US)}"

    val text = s"${lecture.speaker.fullname} (${lecture.speaker.organization}) ${lecture.title}"

    write(date, PDType1Font.HELVETICA_BOLD, 12)
    write(text, PDType1Font.HELVETICA, 12)
  }
}

class PdfBuilder {

  def build(program: Map[String, Seq[ProgramItem]]): Unit = {
    Try {
      val document = PDDocument.load(new File("pdf-template.pdf"))

      val cursor = new ProgramCursor(document, 90, 400)

      program.foreach { case (date, xs) =>
        val formatter = DateTimeFormat.forPattern("dd MM yyyy")
        cursor.writeDate(formatter.parseDateTime(date))
        xs.foreach { item =>
          cursor.writeWorkShopItem(item.item)
          item.sessions.foreach { lecture =>
            cursor.writeLecture(lecture)
          }
        }
      }

      document.save("tmp.pdf")
      document.close()
    }.recover {
      case NonFatal(e) =>
        e.printStackTrace()
    }
  }
}
