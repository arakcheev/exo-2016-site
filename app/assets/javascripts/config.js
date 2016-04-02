(function (requirejs) {
    requirejs.config({
        generateSourceMaps: false,
        packages: ['common', 'modules'],
        shim: {
            'jquery': {
                exports: 'jquery'
            },
            'angular': {
                deps: ['jquery'],
                exports: 'angular'
            },
            'angular-route': ['angular'],
            'angular-cookies': ['angular'],
            'angular-timer': ['angular'],
            'angular-animate': ['angular'],
            'angular-messages': ['angular'],
            'ui-bootstrap': ['angular'],
            'ui-bootstrap-tpls': ['angular']
        },
        paths: {
            'requirejs': ['../lib/requirejs/require'],
            'jquery': ['../lib/jquery/jquery'],
            'angular': ['../lib/angularjs/angular'],
            'angular-route': ['../lib/angularjs/angular-route'],
            'angular-cookies': ['../lib/angularjs/angular-cookies'],
            'angular-animate': ['../lib/angularjs/angular-animate'],
            'angular-messages': ['../lib/angularjs/angular-messages'],
            'underscore': ['../lib/underscorejs/underscore'],
            'ui-bootstrap': ['../lib/angular-ui-bootstrap/ui-bootstrap-tpls']
        }
    });

    require(['angular',
            'angular-cookies',
            'angular-route',
            "angular-animate",
            "angular-messages",
            'ui-bootstrap',
            'jquery',
            'underscore',
            './app'],
        function (angular) {
            angular.element(document).ready(function () {
                angular.bootstrap(document, ['app']);
            });
        }
    );
})(requirejs);