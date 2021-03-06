define(['angular',
        './OnEnter',
        './OnEsc',
        './IncludeReplace',
        './SideMenu',
        './Header',
        './EmailAsyncValidator',
        './PhoneAsyncValidator',
        './AdminHeader'],
    function (angular,
              OnEnter,
              OnEsc,
              IncludeReplace,
              SideMenu,
              Header,
              EmailAsyncValidator,
              PhoneAsyncValidator,
              AdminHeader) {


        return angular.module("common.directives", [])
            .directive('onEnter', OnEnter)
            .directive('onEsc', OnEsc)
            .directive('includeReplace', IncludeReplace)
            .directive('sideMenu', SideMenu)
            .directive('mainHeader', Header)
            .directive('emailValidate', EmailAsyncValidator)
            .directive('phoneValidate', PhoneAsyncValidator)
            .directive('adminHeader', AdminHeader);
    });