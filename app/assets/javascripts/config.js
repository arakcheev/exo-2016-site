(function (requirejs) {
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
            'textAngular': ['angular', 'textAngular-sanitize', 'textAngular-rangy']
        },
        paths: {
            'requirejs': '/assets/lib/requirejs/require.min',
            'angular': '//ajax.googleapis.com/ajax/libs/angularjs/1.4.9/angular.min',
            'angular-route': '//ajax.googleapis.com/ajax/libs/angularjs/1.4.9/angular-route.min',
            'angular-cookies': '//ajax.googleapis.com/ajax/libs/angularjs/1.4.9/angular-cookies.min',
            'angular-animate': '//ajax.googleapis.com/ajax/libs/angularjs/1.4.9/angular-animate.min',
            'angular-messages': '//ajax.googleapis.com/ajax/libs/angularjs/1.4.9/angular-messages.min',
            'underscore': '/assets/lib/underscorejs/underscore-min',
            'ui-bootstrap': '/assets/lib/angular-ui-bootstrap/ui-bootstrap-tpls.min',
            'jsRoutes': "/routes",
            'templates': "/templates",
            'textAngular-sanitize': ['/assets/javascripts/textAngular-sanitize.min'],
            'textAngular-rangy': ['/assets/javascripts/textAngular-rangy.min'],
            'textAngular': ['/assets/javascripts/textAngular.min']
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
            './app'],
        function (angular) {
            angular.element(document).ready(function () {
                angular.bootstrap(document, ['app']);
            });
        }
    );
})(requirejs);