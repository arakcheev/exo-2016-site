(function (requirejs) {

    var isProd = false;

    function ext() {
        if (isProd) return ".min";
        else return "";
    }

    requirejs.config({
        generateSourceMaps: false,
        packages: ['common', 'modules', 'admin'],
        shim: {
            'angular': {
                exports: 'angular'
            },
            'angular-route': ['angular'],
            'angular-cookies': ['angular'],
            'angular-timer': ['angular'],
            'angular-animate': ['angular'],
            'angular-messages': ['angular'],
            'ui-bootstrap': ['angular'],
            'ui-bootstrap-tpls': ['angular'],

            'jsRoutes': {
                exports: 'jsRoutes'
            },
            'templates': {
                exports: 'templates'
            },
            'textAngular-sanitize': ['angular'],
            'textAngular-rangy': ['angular'],
            'textAngular': ['angular', 'textAngular-sanitize', 'textAngular-rangy'],
            'angular-simple-logger': ['angular', 'lodash'],
            'angular-google-map': ['angular', 'google-map', 'lodash']
        },
        paths: {
            'requirejs': '/assets/lib/requirejs/require'+ext(),
            'angular': '//ajax.googleapis.com/ajax/libs/angularjs/1.4.9/angular'+ext(),
            'angular-route': '//ajax.googleapis.com/ajax/libs/angularjs/1.4.9/angular-route'+ext(),
            'angular-cookies': '//ajax.googleapis.com/ajax/libs/angularjs/1.4.9/angular-cookies'+ext(),
            'angular-animate': '//ajax.googleapis.com/ajax/libs/angularjs/1.4.9/angular-animate'+ext(),
            'angular-messages': '//ajax.googleapis.com/ajax/libs/angularjs/1.4.9/angular-messages'+ext(),
            'underscore': '/assets/lib/underscorejs/underscore-min',
            'ui-bootstrap': '/assets/lib/angular-ui-bootstrap/ui-bootstrap-tpls'+ext(),
            'jsRoutes': "/routes",
            'templates': "/templates",
            'textAngular-sanitize': ['/assets/javascripts/textAngular-sanitize.min'],
            'textAngular-rangy': ['/assets/javascripts/textAngular-rangy.min'],
            'textAngular': ['/assets/javascripts/textAngular.min'],
            'lodash': '//cdn.jsdelivr.net/lodash/4.8.2/lodash'+ext(),
            'angular-simple-logger': '/assets/javascripts/angular-simple-logger',
            'angular-google-map': '/assets/javascripts/angular-google-map',
            'google-map': 'https://maps.googleapis.com/maps/api/js?sensor=false'

        }
    });

    require(['angular',
            'angular-cookies',
            'angular-route',
            "angular-animate",
            "angular-messages",
            'ui-bootstrap',
            'underscore',
            'jsRoutes',
            'templates',
            'textAngular-rangy',
            'textAngular-sanitize',
            'textAngular',
            'lodash',
            'angular-simple-logger',
            'google-map',
            'angular-google-map',
            './app'],
        function (angular) {
            angular.element(document).ready(function () {
                angular.bootstrap(document, ['app']);
            });
        }
    );
})(requirejs);