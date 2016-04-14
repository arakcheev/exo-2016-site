/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

package services

import java.io.File
import java.util
import java.util.Locale

import controllers.ProgramItem
import models.{Lecture, WorkShopItem}
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.pdmodel.font.PDType1Font
import org.apache.pdfbox.pdmodel.{PDDocument, PDPage, PDPageContentStream}
import org.joda.time.{DateTimeZone, DateTime}

import scala.collection.mutable.ArrayBuffer
import scala.util.Try

case class Page(top: Float, left: Float, right: Float, bottom: Float, height: Float)

object Page {
  def default: Page = {
    Page(10, 10, 10, 10, 100)
  }
}

class PageBuilder(document: PDDocument, page: PDPage) {

  def writeDate(dateTime: DateTime, yPosition: Float, xPosition: Float = 93) = {
    val content = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true)
    content.setNonStrokingColor(0, 0, 0)
    content.beginText()
    content.setFont(PDType1Font.HELVETICA_BOLD, 14)
    content.newLineAtOffset(xPosition, yPosition)
    content.showText(dateTime.toString(PdfBuilder.DATE_FORMATTER, Locale.US))
    content.endText()
    content.close()
  }

  def writeWorkShopItem(workShopItem: WorkShopItem, yPosition: Float, xPosition: Float = 93) = {
    val text = s"${workShopItem.title} ${workShopItem.startDate.toString(PdfBuilder.WORKSHOP_DATE_FORMATTER, Locale.US)} - ${workShopItem.endDate.toString(PdfBuilder.WORKSHOP_DATE_FORMATTER, Locale.US)}"

    val content = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true)
    content.setNonStrokingColor(0, 0, 0)
    content.beginText()
    content.setFont(PDType1Font.HELVETICA_BOLD, 13)
    content.newLineAtOffset(0, -1)
    content.showText(text)
    content.endText()
    content.close()
  }

  def writeSession(lecture: Lecture) = {

    val date = s"${lecture.date.toString(PdfBuilder.WORKSHOP_DATE_FORMATTER, Locale.US)}"

    val text = s"${lecture.speaker.fullname} (${lecture.speaker.organization}) - ${lecture.title}"

    val content = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true)
    content.setNonStrokingColor(0, 0, 0)
    content.beginText()
    content.setFont(PDType1Font.HELVETICA_BOLD, 12)
    content.newLineAtOffset(0, -1)
    content.showText(date)
    content.newLine()

    PdfBuilder.writeLongText(content, PDType1Font.HELVETICA, 12, -1, page.getMediaBox, text)

    content.endText()
    content.close()
  }
}

object PdfBuilder {

  val DATE_FORMATTER = "EEEEE, d MMMMM"

  val WORKSHOP_DATE_FORMATTER = "HH : mm"

  def getFontHeight(font: PDType1Font, size: Float): Float = {
    PDType1Font.HELVETICA_BOLD.getFontDescriptor.getFontBoundingBox.getHeight / 1000 * size
  }

  /**
    *
    * @see http://stackoverflow.com/a/19683618
    */
  def writeLongText(contentStream: PDPageContentStream,
                    font: PDType1Font,
                    fontSize: Float,
                    yMargin: Float,
                    box: PDRectangle,
                    txt: String) = {

    val leading = 1.2f * fontSize
    var text = txt

    val leftMargin = 93
    val rightMargin = 30

    val width = box.getWidth - leftMargin - rightMargin
    //    val startX = box.getLowerLeftX + leftMargin
    //    val startY = box.getUpperRightY - yMargin

    val lines = new ArrayBuffer[String]()
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

    contentStream.setFont(font, fontSize)
    //    contentStream.newLineAtOffset(startX, startY)
    for (line <- lines) {
      contentStream.showText(line)
      contentStream.newLineAtOffset(0, -leading)
    }
  }
}

class PdfBuilder {


  def build(program: Map[DateTime, Seq[ProgramItem]]): Unit = {
    Try {
      val document = PDDocument.load(new File("pdf-template.pdf"))

      val page = new PDPage()

      document.addPage(page)

      val b = new PageBuilder(document, page)

      program.foreach { case (date, xs) =>
        b.writeDate(date, page.getMediaBox.getUpperRightY - 50)
//        xs.foreach { item =>
//          b.writeWorkShopItem(item.item, 100)
//          item.sessions.foreach { lecture =>
//            b.writeSession(lecture)
//          }
//        }
      }


      document.save("tmp.pdf")
      document.close()
    }
  }
}
