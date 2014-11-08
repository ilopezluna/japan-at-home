'use strict';

/* Controllers */

japanAtHomeApp.controller('MainController', function ($scope) {
    });

japanAtHomeApp.controller('AdminController', function ($scope) {
    });

japanAtHomeApp.controller('LanguageController', function ($scope, $translate, LanguageService) {

        $scope.isAvailable = function (key) {
            return key == 'es' || key == 'ca';
        };

        $scope.changeLanguage = function (languageKey) {
            $translate.use(languageKey);

            LanguageService.getBy(languageKey).then(function(languages) {
                $scope.languages = languages;
            });
        };

        LanguageService.getBy().then(function (languages) {
            $scope.languages = languages;
        });
    });

japanAtHomeApp.controller('MenuController', function ($scope) {
    });

japanAtHomeApp.controller('HomeController', function ($scope, Restaurant, OrderService, $location)
{
    $scope.restaurants = Restaurant.query();
    $scope.menu = function (restaurant)
    {
        if ($scope.order.zip.code == null) {
            alert("Necesitamos saber tú codigo postal"); //TODO quitar alert
        }
        else {
            for(var i = 0; i < restaurant.zips.length; i += 1) {
                if(restaurant.zips[i]['code'] === $scope.order.zip.code) {
                    $scope.order.zip = restaurant.zips[i];
                }
            }
            OrderService.restaurant(restaurant);
            $location.path("/restaurant/" + restaurant.id + "/" + restaurant.shortName + "/menu");
        }
    };
});


japanAtHomeApp.controller('RestaurantMenuController', function ($scope, Restaurant, Product, Tag, $routeParams, OrderService)
{
    $scope.restaurant = Restaurant.get( {id: $routeParams.restaurantId} , function(data) {
        OrderService.restaurant($scope.restaurant);
    });

    $scope.categories = Tag.query({restaurantId: $routeParams.restaurantId});
    $scope.products = Product.query({restaurantId: $routeParams.restaurantId});

    $scope.selected = 0;
    $scope.select= function(index) {
        $scope.selected = index;
    };
});

japanAtHomeApp.controller('SuggestController', function ($scope, Suggestion)
{
    $scope.suggest = function (suggestion)
    {
        if (suggestion != null && suggestion.description != null)
        {
            Suggestion.save(suggestion);
        }
    }
});

japanAtHomeApp.controller('CartController', function ($scope, $location, Order, OrderService) {

    $scope.order = OrderService.current();

    $scope.addItem = function( product )
    {
        if ( product.id in $scope.order.items)
        {
            $scope.order.items[product.id].quantity++;
        }
        else
        {
            var item = new Object();
            item.product = product;
            item.quantity = 1;

            $scope.order.items[product.id] = item;
        }
    };

    $scope.removeItem = function(item)
    {
        if ( $scope.order.items[item.product.id].quantity == 1 )
        {
            delete $scope.order.items[item.product.id];
        }
        else
        {
            $scope.order.items[item.product.id].quantity--;
        }
    };

    $scope.total = function()
    {
        var total = 0;

        angular.forEach($scope.order.items, function(item)
        {
            var toSum = item.quantity * item.product.price;
            total += toSum;
        });

        return total.toFixed(2);
    };

    $scope.menu = function(restaurant)
    {
        $location.path("/restaurant/" + restaurant.id + "/" + restaurant.shortName + "/menu");
    };

    $scope.delivery = function(restaurant)
    {
        $location.path("/restaurant/" + restaurant.id + "/" + restaurant.shortName + "/resume");
    };

    $scope.getItemPrice = function(item)
    {
        return (item.product.price * item.quantity).toFixed(2);
    };
});

japanAtHomeApp.controller('MerchantController', function ($scope, Order, $sce, $http) {

    $http.get('/app/rest/orders/payment/url')
        .success(function (data) {
            $scope.url = data;
        });

    $scope.trustSrc = function(src) {
        return $sce.trustAsResourceUrl(src);
    }
    $scope.create = function (paymentType)
    {
        $scope.order.code = Math.floor(Math.random() * 1000000000000);
        $scope.order.paymentType = paymentType;

        Order.save($scope.order,
            function (merchant)
            {
                $scope.merchant = merchant;
            });
    };

    $scope.submmit = function ()
    {
        merchantForm.submit(); //TODO This is not the angular way, so look for a better choice
    };
});

japanAtHomeApp.controller('DeliveryController', function ($scope, OrderService, Restaurant, $routeParams) {
    $scope.restaurant = Restaurant.get( {id: $routeParams.restaurantId} , function(data) {
        OrderService.restaurant($scope.restaurant);
    });
});


japanAtHomeApp.controller('LoginController', function ($scope, $location, AuthenticationSharedService) {
        $scope.rememberMe = true;
        $scope.login = function () {
            AuthenticationSharedService.login({
                username: $scope.username,
                password: $scope.password,
                rememberMe: $scope.rememberMe
            });
        }
    });

japanAtHomeApp.controller('LogoutController', function ($location, AuthenticationSharedService) {
        AuthenticationSharedService.logout();
    });

japanAtHomeApp.controller('SettingsController', function ($scope, Account) {
        $scope.success = null;
        $scope.error = null;
        $scope.settingsAccount = Account.get();

        $scope.save = function () {
            Account.save($scope.settingsAccount,
                function (value, responseHeaders) {
                    $scope.error = null;
                    $scope.success = 'OK';
                    $scope.settingsAccount = Account.get();
                },
                function (httpResponse) {
                    $scope.success = null;
                    $scope.error = "ERROR";
                });
        };
    });

japanAtHomeApp.controller('RegisterController', function ($scope, $translate, Register) {
        $scope.success = null;
        $scope.error = null;
        $scope.doNotMatch = null;
        $scope.errorUserExists = null;
        $scope.register = function () {
            if ($scope.registerAccount.password != $scope.confirmPassword) {
                $scope.doNotMatch = "ERROR";
            } else {
                $scope.registerAccount.langKey = $translate.use();
                $scope.doNotMatch = null;
                Register.save($scope.registerAccount,
                    function (value, responseHeaders) {
                        $scope.error = null;
                        $scope.errorUserExists = null;
                        $scope.success = 'OK';
                    },
                    function (httpResponse) {
                        $scope.success = null;
                        if (httpResponse.status === 304 &&
                                httpResponse.data.error && httpResponse.data.error === "Not Modified") {
                            $scope.error = null;
                            $scope.errorUserExists = "ERROR";
                        } else {
                            $scope.error = "ERROR";
                            $scope.errorUserExists = null;
                        }
                    });
            }
        }
    });

japanAtHomeApp.controller('ActivationController', function ($scope, $routeParams, Activate) {
        Activate.get({key: $routeParams.key},
            function (value, responseHeaders) {
                $scope.error = null;
                $scope.success = 'OK';
            },
            function (httpResponse) {
                $scope.success = null;
                $scope.error = "ERROR";
            });
    });

japanAtHomeApp.controller('PasswordController', function ($scope, Password) {
        $scope.success = null;
        $scope.error = null;
        $scope.doNotMatch = null;
        $scope.changePassword = function () {
            if ($scope.password != $scope.confirmPassword) {
                $scope.doNotMatch = "ERROR";
            } else {
                $scope.doNotMatch = null;
                Password.save($scope.password,
                    function (value, responseHeaders) {
                        $scope.error = null;
                        $scope.success = 'OK';
                    },
                    function (httpResponse) {
                        $scope.success = null;
                        $scope.error = "ERROR";
                    });
            }
        };
    });

japanAtHomeApp.controller('SessionsController', function ($scope, resolvedSessions, Sessions) {
        $scope.success = null;
        $scope.error = null;
        $scope.sessions = resolvedSessions;
        $scope.invalidate = function (series) {
            Sessions.delete({series: encodeURIComponent(series)},
                function (value, responseHeaders) {
                    $scope.error = null;
                    $scope.success = "OK";
                    $scope.sessions = Sessions.get();
                },
                function (httpResponse) {
                    $scope.success = null;
                    $scope.error = "ERROR";
                });
        };
    });

 japanAtHomeApp.controller('HealthController', function ($scope, HealthCheckService) {
     $scope.updatingHealth = true;

     $scope.refresh = function() {
         $scope.updatingHealth = true;
         HealthCheckService.check().then(function(promise) {
             $scope.healthCheck = promise;
             $scope.updatingHealth = false;
         },function(promise) {
             $scope.healthCheck = promise.data;
             $scope.updatingHealth = false;
         });
     }

     $scope.refresh();

     $scope.getLabelClass = function(statusState) {
         if (statusState == 'UP') {
             return "label-success";
         } else {
             return "label-danger";
         }
     }
 });

japanAtHomeApp.controller('MetricsController', function ($scope, MetricsService, HealthCheckService, ThreadDumpService) {
        $scope.metrics = {};
		$scope.updatingMetrics = true;

        $scope.refresh = function() {
			$scope.updatingMetrics = true;
			MetricsService.get().then(function(promise) {
        		$scope.metrics = promise;
				$scope.updatingMetrics = false;
        	},function(promise) {
        		$scope.metrics = promise.data;
				$scope.updatingMetrics = false;
        	});
        };

		$scope.$watch('metrics', function(newValue, oldValue) {
			$scope.servicesStats = {};
            $scope.cachesStats = {};
            angular.forEach(newValue.timers, function(value, key) {
                if (key.indexOf("web.rest") != -1 || key.indexOf("service") != -1) {
                    $scope.servicesStats[key] = value;
                }

                if (key.indexOf("net.sf.ehcache.Cache") != -1) {
                    // remove gets or puts
                    var index = key.lastIndexOf(".");
                    var newKey = key.substr(0, index);

                    // Keep the name of the domain
                    index = newKey.lastIndexOf(".");
                    $scope.cachesStats[newKey] = {
                        'name': newKey.substr(index + 1),
                        'value': value
                    };
                };
            });
		});

        $scope.refresh();

        $scope.threadDump = function() {
            ThreadDumpService.dump().then(function(data) {
                $scope.threadDump = data;

                $scope.threadDumpRunnable = 0;
                $scope.threadDumpWaiting = 0;
                $scope.threadDumpTimedWaiting = 0;
                $scope.threadDumpBlocked = 0;

                angular.forEach(data, function(value, key) {
                    if (value.threadState == 'RUNNABLE') {
                        $scope.threadDumpRunnable += 1;
                    } else if (value.threadState == 'WAITING') {
                        $scope.threadDumpWaiting += 1;
                    } else if (value.threadState == 'TIMED_WAITING') {
                        $scope.threadDumpTimedWaiting += 1;
                    } else if (value.threadState == 'BLOCKED') {
                        $scope.threadDumpBlocked += 1;
                    }
                });

                $scope.threadDumpAll = $scope.threadDumpRunnable + $scope.threadDumpWaiting +
                    $scope.threadDumpTimedWaiting + $scope.threadDumpBlocked;

            });
        };

        $scope.getLabelClass = function(threadState) {
            if (threadState == 'RUNNABLE') {
                return "label-success";
            } else if (threadState == 'WAITING') {
                return "label-info";
            } else if (threadState == 'TIMED_WAITING') {
                return "label-warning";
            } else if (threadState == 'BLOCKED') {
                return "label-danger";
            }
        };
    });

japanAtHomeApp.controller('LogsController', function ($scope, resolvedLogs, LogsService) {
        $scope.loggers = resolvedLogs;

        $scope.changeLevel = function (name, level) {
            LogsService.changeLevel({name: name, level: level}, function () {
                $scope.loggers = LogsService.findAll();
            });
        }
    });

japanAtHomeApp.controller('AuditsController', function ($scope, $translate, $filter, AuditsService) {
        $scope.onChangeDate = function() {
            AuditsService.findByDates($scope.fromDate, $scope.toDate).then(function(data){
                $scope.audits = data;
            });
        };

        // Date picker configuration
        $scope.today = function() {
            // Today + 1 day - needed if the current day must be included
            var today = new Date();
            var tomorrow = new Date(today.getFullYear(), today.getMonth(), today.getDate()+1); // create new increased date

            $scope.toDate = $filter('date')(tomorrow, "yyyy-MM-dd");
        };

        $scope.previousMonth = function() {
            var fromDate = new Date();
            if (fromDate.getMonth() == 0) {
                fromDate = new Date(fromDate.getFullYear() - 1, 0, fromDate.getDate());
            } else {
                fromDate = new Date(fromDate.getFullYear(), fromDate.getMonth() - 1, fromDate.getDate());
            }

            $scope.fromDate = $filter('date')(fromDate, "yyyy-MM-dd");
        };

        $scope.today();
        $scope.previousMonth();

        AuditsService.findByDates($scope.fromDate, $scope.toDate).then(function(data){
            $scope.audits = data;
        });
    });
