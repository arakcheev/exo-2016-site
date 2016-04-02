define(['angular',
    './OnEnter',
    './OnEsc',
    './IncludeReplace',
    './SideMenu',
    './Header'], function (angular,
                           OnEnter,
                           OnEsc,
                           IncludeReplace,
                           SideMenu,
                           Header) {


    return angular.module("common.directives", [])
        .directive('onEnter', OnEnter)
        .directive('onEsc', OnEsc)
        .directive('includeReplace', IncludeReplace)
        .directive('sideMenu', SideMenu)
        .directive('mainHeader', Header);
});