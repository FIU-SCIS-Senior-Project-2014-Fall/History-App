(
    function() {
        "use strict";
        angular.module("app", ["ngRoute","ngAnimate","ui.bootstrap","textAngular",])

        //----------CONFIGURATIONS---------//
        .config(["$routeProvider",
            function($routeProvider) {
                var routes, setRoutes;
                return routes = ["dashboard/dashboard",
                    "layout/404",
                    "layout/500",
                    "layout/feedback",
                    "layout/blank",
                    "layout/forgot-password",
                    "layout/signin",
                    "layout/signup",
                    "layout/thankyou",
                    "mail/compose",
                    "mail/inbox",
                    "mail/single",
                    "places/step0",
                    "places/step1",
                    "places/step2",
                    "places/step3",
                    "system_access/users",
                    "system_access/groups",
                    "system_access/permissions",
                    "tasks/tasks"
                ], setRoutes = function(route) {
                    var config, url;
                    return url = "/" + route, config = {
                        templateUrl: "views/" + route + ".html"
                    }, $routeProvider.when(url, config), $routeProvider
                }, routes.forEach(function(route) {
                    return setRoutes(route)
                }), $routeProvider.when("/", {
                    redirectTo: "/layout/signin"
                }), $routeProvider.when("/places", {
                    redirectTo: "/places/step0"
                }).when("/404", {
                    templateUrl: "views/layout/404.html"
                }).otherwise({
                    redirectTo: "/404"
                })
            }
        ])

        //------------FACTORIES-----------//
        .factory("logger", [function() {
                var logIt;
                return toastr.options = {
                    closeButton: !0,
                    positionClass: "toast-bottom-right",
                    timeOut: "3000"
                }, logIt = function(message, type) {
                    return toastr[type](message)
                }, {
                    log: function(message) {
                        logIt(message, "info")
                    },
                    logWarning: function(message) {
                        logIt(message, "warning")
                    },
                    logSuccess: function(message) {
                        logIt(message, "success")
                    },
                    logError: function(message) {
                        logIt(message, "error")
                    }
                }
        }])
        .factory("localize", ["$http", "$rootScope", "$window",
                function($http, $rootScope, $window) {
                    var localize;
                    return localize = {
                        language: "",
                        url: void 0,
                        resourceFileLoaded: !1,
                        successCallback: function(data) {
                            return localize.dictionary = data, localize.resourceFileLoaded = !0, $rootScope.$broadcast("localizeResourcesUpdated")
                        },
                        setLanguage: function(value) {
                            return localize.language = value.toLowerCase().split("-")[0], localize.initLocalizedResources()
                        },
                        setUrl: function(value) {
                            return localize.url = value, localize.initLocalizedResources()
                        },
                        buildUrl: function() {
                            return localize.language || (localize.language = ($window.navigator.userLanguage || $window.navigator.language).toLowerCase(), localize.language = localize.language.split("-")[0]), "i18n/resources-locale_" + localize.language + ".js"
                        },
                        initLocalizedResources: function() {
                            var url;
                            return url = localize.url || localize.buildUrl(), $http({
                                method: "GET",
                                url: url,
                                cache: !1
                            }).success(localize.successCallback).error(function() {
                                return $rootScope.$broadcast("localizeResourcesUpdated")
                            })
                        },
                        getLocalizedString: function(value) {
                            var result, valueLowerCase;
                            return result = void 0, localize.dictionary && value ? (valueLowerCase = value.toLowerCase(), result = "" === localize.dictionary[valueLowerCase] ? value : localize.dictionary[valueLowerCase]) : result = value, result
                        }
                    }
                }
        ])
        .factory("taskStorage", function() {
                var DEMO_TASKS, STORAGE_ID;
                return STORAGE_ID = "tasks", DEMO_TASKS = '[ {"title": "Finish homework", "completed": true}, {"title": "Try Google glass", "completed": false}, {"title": "Make a call", "completed": true}, {"title": "Build a snowman :)", "completed": false}, {"title": "Apply for monster university!", "completed": false}, {"title": "Play games with friends", "completed": true}, {"title": "Learn Swift", "completed": false}, {"title": "Shopping", "completed": false} ]', {
                    get: function() {
                        return JSON.parse(localStorage.getItem(STORAGE_ID) || DEMO_TASKS)
                    },
                    put: function(tasks) {
                        return localStorage.setItem(STORAGE_ID, JSON.stringify(tasks))
                    }
                }
        })

        //-----------DIRECTIVES----------//
        .directive("i18n", ["localize",
                function(localize) {
                    var i18nDirective;
                    return i18nDirective = {
                        restrict: "EA",
                        updateText: function(ele, input, placeholder) {
                            var result;
                            return result = void 0, "i18n-placeholder" === input ? (result = localize.getLocalizedString(placeholder), ele.attr("placeholder", result)) : input.length >= 1 ? (result = localize.getLocalizedString(input), ele.text(result)) : void 0
                        },
                        link: function(scope, ele, attrs) {
                            return scope.$on("localizeResourcesUpdated", function() {
                                return i18nDirective.updateText(ele, attrs.i18n, attrs.placeholder)
                            }), attrs.$observe("i18n", function(value) {
                                return i18nDirective.updateText(ele, value, attrs.placeholder)
                            })
                        }
                    }
                }
        ])
        .directive("toggleNavCollapsedMin", ["$rootScope",
                function($rootScope) {
                    return {
                        restrict: "A",
                        link: function(scope, ele) {
                            var app;
                            return app = $("#app"), ele.on("click", function(e) {
                                return app.hasClass("nav-collapsed-min") ? app.removeClass("nav-collapsed-min") : (app.addClass("nav-collapsed-min"), $rootScope.$broadcast("nav:reset")), e.preventDefault()
                            })
                        }
                    }
                }
        ])
        .directive("collapseNav", [
                function() {
                    return {
                        restrict: "A",
                        link: function(scope, ele) {
                            var $a, $aRest, $app, $lists, $listsRest, $nav, $window, Timer, prevWidth, updateClass;
                            return $window = $(window), $lists = ele.find("ul").parent("li"), $lists.append('<i class="fa fa-angle-down icon-has-ul-h"></i><i class="fa fa-angle-right icon-has-ul"></i>'), $a = $lists.children("a"), $listsRest = ele.children("li").not($lists), $aRest = $listsRest.children("a"), $app = $("#app"), $nav = $("#nav-container"), $a.on("click", function(event) {
                                var $parent, $this;
                                return $app.hasClass("nav-collapsed-min") || $nav.hasClass("nav-horizontal") && $window.width() >= 768 ? !1 : ($this = $(this), $parent = $this.parent("li"), $lists.not($parent).removeClass("open").find("ul").slideUp(), $parent.toggleClass("open").find("ul").slideToggle(), event.preventDefault())
                            }), $aRest.on("click", function() {
                                return $lists.removeClass("open").find("ul").slideUp()
                            }), scope.$on("nav:reset", function() {
                                return $lists.removeClass("open").find("ul").slideUp()
                            }), Timer = void 0, prevWidth = $window.width(), updateClass = function() {
                                var currentWidth;
                                return currentWidth = $window.width(), 768 > currentWidth && $app.removeClass("nav-collapsed-min"), 768 > prevWidth && currentWidth >= 768 && $nav.hasClass("nav-horizontal") && $lists.removeClass("open").find("ul").slideUp(), prevWidth = currentWidth
                            }, $window.resize(function() {
                                var t;
                                return clearTimeout(t), t = setTimeout(updateClass, 300)
                            })
                        }
                    }
                }
        ])
        .directive("highlightActive", [
                function() {
                    return {
                        restrict: "A",
                        controller: ["$scope", "$element", "$attrs", "$location",
                            function($scope, $element, $attrs, $location) {
                                var highlightActive, links, path;
                                return links = $element.find("a"),
                                    path = function() {
                                        return $location.path()
                                    }, highlightActive = function(links, path) {
                                        return path = "#" + path, angular.forEach(links, function(link) {
                                            var $li, $link, href;
                                            return $link = angular.element(link),
                                                $li = $link.parent("li"),
                                                href = $link.attr("href"),

                                                $li.hasClass("active") && $li.removeClass("active"),

                                                0 === path.indexOf(href) ? $li.addClass("active") : void 0

                                        })
                                    }, highlightActive(links, $location.path()), $scope.$watch(path, function(newVal, oldVal) {
                                        return newVal !== oldVal ? highlightActive(links, $location.path()) : void 0
                                    })
                            }
                        ]
                    }
                }
        ])
        .directive("taskFocus", ["$timeout",
                function($timeout) {
                    return {
                        link: function(scope, ele, attrs) {
                            return scope.$watch(attrs.taskFocus, function(newVal) {
                                return newVal ? $timeout(function() {
                                    return ele[0].focus()
                                }, 0, !1) : void 0
                            })
                        }
                    }
                }
        ])
        .directive("flotChart", [function() {
                return {
                    restrict: "A",
                    scope: {
                        data: "=",
                        options: "="
                    },
                    link: function(scope, ele) {
                        var data, options, plot;
                        return data = scope.data, options = scope.options, plot = $.plot(ele[0], data, options)
                    }
                }
        }])
        .directive("flotChartRealtime", [function() {
                return {
                    restrict: "A",
                    link: function(scope, ele) {
                        var data, getRandomData, plot, totalPoints, update, updateInterval;
                        return data = [], totalPoints = 300, getRandomData = function() {
                            var i, prev, res, y;
                            for (data.length > 0 && (data = data.slice(1)); data.length < totalPoints;) prev = data.length > 0 ? data[data.length - 1] : 50, y = prev + 10 * Math.random() - 5, 0 > y ? y = 0 : y > 100 && (y = 100), data.push(y);
                            for (res = [], i = 0; i < data.length;) res.push([i, data[i]]), ++i;
                            return res
                        }, update = function() {
                            plot.setData([getRandomData()]), plot.draw(), setTimeout(update, updateInterval)
                        }, data = [], totalPoints = 300, updateInterval = 200, plot = $.plot(ele[0], [getRandomData()], {
                            series: {
                                lines: {
                                    show: !0,
                                    fill: !0
                                },
                                shadowSize: 0
                            },
                            yaxis: {
                                min: 0,
                                max: 100
                            },
                            xaxis: {
                                show: !1
                            },
                            grid: {
                                hoverable: !0,
                                borderWidth: 1,
                                borderColor: "#eeeeee"
                            },
                            colors: ["#70b1cf"]
                        }), update()
                    }
                }
        }])
        .directive("customPage", function() {
                return {
                    restrict: "A",
                    controller: ["$scope", "$element", "$location",
                        function($scope, $element, $location) {
                            var addBg, path;
                            return path = function() {
                                return $location.path()
                            }, addBg = function(path) {
                                switch ($element.removeClass("body-wide body-lock"), path) {
                                    case "/404":
                                    case "/layout/404":
                                    case "/layout/500":
                                    case "/layout/signin":
                                    case "/layout/signup":
                                    case "/layout/feedback":
                                    case "/layout/thankyou":
                                    case "/layout/forgot-password":
                                        return $element.addClass("body-wide");
                                }
                            }, addBg($location.path()), $scope.$watch(path, function(newVal, oldVal) {
                                return newVal !== oldVal ? addBg($location.path()) : void 0
                            })
                        }
                    ]
                }
        })
        
        //-----------SERVICES----------//
        .service('wizzardService', function() {
          var currentPlace;
          var setCurrentPlace = function(place) {
              currentPlace = place;
          }
          var getCurrentPlace = function(){
              return currentPlace;
          }
          return {
            setCurrentPlace: setCurrentPlace,
            getCurrentPlace: getCurrentPlace
          };
        })

        //----------CONTROLLERS---------//
        .controller("tableCtrl", ["$scope", "$filter", "$http", "$route", "$location", "$timeout",
                function($scope, $filter, $http, $route, $location, $timeout) {
                    $scope.places = [];
                    var init;

                    $http.get('../WebApp/rest/getAllPlaces.php').then(function(response) {
                        ($scope.places = response.data, null)
                    })

                    return $scope.lastOrder = "", $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[0], $scope.currentPage = 1, $scope.currentPagePlaces = [], $scope.searchKeywords = "", $scope.filteredPlaces = [], $scope.row = "",
                        $scope.editPlace = function(place) {
                            alert(place.PlaceId);
                        },
                        $scope.deletePlace = function(place) {
                            $http({
                                    url: 'http://ha-dev.cis.fiu.edu/WebApp/rest/deletePlace.php',
                                    method: "POST",
                                    data: {
                                        'id': place.PlaceId
                                    }
                                })
                                .then(
                                    function(response) {
                                        $route.reload()
                                    },
                                    function(response) {
                                        alert("Request to delete Place with PlaceId:" + place.PlaceId + " has failed!");
                                    }
                                );
                        },
                        $scope.reloadTable = function() {
                            $route.reload();
                        },
                        $scope.initWizard = function() {
                            $location.path('/places/step1');
                        },
                        $scope.onFilterChange = function() {
                            return $scope.select(1),
                                $scope.currentPage = 1,
                                $scope.row = ""
                        },
                        $scope.onNumPerPageChange = function() {
                            return $scope.select(1),
                                $scope.currentPage = 1
                        },
                        $scope.onOrderChange = function() {
                            return $scope.select(1),
                                $scope.currentPage = 1
                        },
                        $scope.search = function() {
                            return $scope.filteredPlaces = $filter("filter")($scope.places, $scope.searchKeywords),
                                $scope.onFilterChange()
                        },
                        $scope.order = function(rowName) {
                            return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredPlaces = $filter("orderBy")($scope.places, rowName),
                                $scope.onOrderChange()) : void 0
                        },
                        $scope.defaultOrder = function(rowName) {
                            $scope.lastOrder = $scope.lastOrder == "-" ? "" : "-";

                            rowName = $scope.lastOrder + rowName;

                            return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredPlaces = $filter("orderBy")($scope.places, rowName),
                                $scope.onOrderChange()) : void 0
                        },
                        $scope.select = function(page) {
                            var end, start;

                            return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPagePlaces = $scope.filteredPlaces.slice(start, end)
                        },
                        $timeout(init = function() {
                            return $scope.search(), $scope.select($scope.currentPage)
                        }, 400, true)
                }
        ])
        .controller("taskCtrl", ["$scope", "taskStorage", "filterFilter", "$rootScope", "logger",
                function($scope, taskStorage, filterFilter, $rootScope, logger) {
                    var tasks;
                    return tasks = $scope.tasks = taskStorage.get(), $scope.newTask = "", $scope.remainingCount = filterFilter(tasks, {
                        completed: !1
                    }).length, $scope.editedTask = null, $scope.statusFilter = {
                        completed: !1
                    }, $scope.filter = function(filter) {
                        switch (filter) {
                            case "all":
                                return $scope.statusFilter = "";
                            case "active":
                                return $scope.statusFilter = {
                                    completed: !1
                                };
                            case "completed":
                                return $scope.statusFilter = {
                                    completed: !0
                                }
                        }
                    }, $scope.add = function() {
                        var newTask;
                        return newTask = $scope.newTask.trim(), 0 !== newTask.length ? (tasks.push({
                            title: newTask,
                            completed: !1
                        }), logger.logSuccess('New task: "' + newTask + '" added'), taskStorage.put(tasks), $scope.newTask = "", $scope.remainingCount++) : void 0
                    }, $scope.edit = function(task) {
                        return $scope.editedTask = task
                    }, $scope.doneEditing = function(task) {
                        return $scope.editedTask = null, task.title = task.title.trim(), task.title ? logger.log("Task updated") : $scope.remove(task), taskStorage.put(tasks)
                    }, $scope.remove = function(task) {
                        var index;
                        return $scope.remainingCount -= task.completed ? 0 : 1, index = $scope.tasks.indexOf(task), $scope.tasks.splice(index, 1), taskStorage.put(tasks), logger.logError("Task removed")
                    }, $scope.completed = function(task) {
                        return $scope.remainingCount += task.completed ? -1 : 1, taskStorage.put(tasks), task.completed ? $scope.remainingCount > 0 ? logger.log(1 === $scope.remainingCount ? "Almost there! Only " + $scope.remainingCount + " task left" : "Good job! Only " + $scope.remainingCount + " tasks left") : logger.logSuccess("Congrats! All done :)") : void 0
                    }, $scope.clearCompleted = function() {
                        return $scope.tasks = tasks = tasks.filter(function(val) {
                            return !val.completed
                        }), taskStorage.put(tasks)
                    }, $scope.markAll = function(completed) {
                        return tasks.forEach(function(task) {
                            return task.completed = completed
                        }), $scope.remainingCount = completed ? 0 : tasks.length, taskStorage.put(tasks), completed ? logger.logSuccess("Congrats! All done :)") : void 0
                    }, $scope.$watch("remainingCount == 0", function(val) {
                        return $scope.allChecked = val
                    }), $scope.$watch("remainingCount", function(newVal) {
                        return $rootScope.$broadcast("taskRemaining:changed", newVal)
                    })
                }
        ])
        .controller("AppCtrl", ["$scope", "$rootScope", "$http", "$location", "$timeout", "$compile", "wizzardService",
                function($scope, $rootScope, $http, $location, $timeout, $compile, wizzardService) {

                    var map;
                    var infowindow;
                    var delay = "100";

                    $scope.init = function() {

                        var isInWizzard = $location.path().toLowerCase().indexOf("/step") > 0;
                        var isWizzardSplash = $location.path().toLowerCase().indexOf("/step0") > 0;

                        $scope.initHeader();
                        $scope.initNav();
                        $scope.initDashboard();

                        if (isInWizzard && !isWizzardSplash)
                            $scope.activatePlacesWizzardControls();
                        else
                            $scope.deactivatePlacesWizzardControls();
                    }

                    $scope.initHeader = function() {
                        $http.get('../WebApp/rest/getNumberOfUnreadMessages.php').then(function(response) {
                            ($scope.mail.unread = response.data[0]['feedbackNo'], null)
                        })
                    }

                    $scope.initNav = function() {

                    }

                    $scope.initDashboard = function() {
                        $http.get('../WebApp/rest/getNumberOfPlacesCreatedToday.php').then(function(response) {
                            ($scope.dash.placesCreatedTodayNo = response.data[0]['placesCreatedToday'], null)
                        })
                    }

                    $scope.activatePlacesWizzardControls = function() {
                        $scope.places.showWizzardControls = "";
                        $(".placesWizzardControls").removeClass("active");
                        var currentStep = parseInt($location.path().split('/places/step')[1]);
                        $scope.places.wizzardControlAdd = "hidden";
                        $scope.places.wizzardControlNext = currentStep == 3 ? 0 : currentStep + 1;
                        $scope.places.wizzardControlBack = currentStep == 0 ? currentStep : currentStep - 1;
                        $scope.places.ShowConfirm = "";

                        if (currentStep == 1)
                            $timeout(function() {
                                $scope.loadGoogleMap();
                            }, delay);

                        if (currentStep == 3) {
                            $scope.getCurrentPlaceDataFromDb();
                            $scope.places.wizzardNextFA = "fa fa-check";
                            $scope.places.wizzardNextText = "Done";
                        } else {
                            if (currentStep == 2) {
                                $scope.getCurrentPlaceDataFromDb();
                                $scope.fillOutContactInformation();
                            }
                            $scope.places.wizzardNextFA = "fa fa-caret-right";
                            $scope.places.wizzardNextText = "Next";
                        }
                    }

                    $scope.getCurrentPlaceDataFromDb = function() {
                        $http.get('../WebApp/rest/getCurrentPlace.php').then(function(response) {
                            ($scope.placeInfo = response.data[0], null)
                        });
                    }

                    $scope.deactivatePlacesWizzardControls = function() {
                        var currentStep = parseInt($location.path().split('/places/step')[1]);
                        $scope.places.showWizzardControls = "hidden";
                        $scope.places.wizzardControlAdd = "";
                        $scope.places.ShowConfirm = "hidden";

                        if (currentStep == 0)
                            $timeout(function() {
                                $scope.loadGoogleMap();
                            }, delay);
                    }


                    $scope.addTimelineEvent = function() {

                        $scope.timeline.side = $scope.timeline.side == "alt" ? "" : "alt";

                        var html = '<article class="tl-item ' + $scope.timeline.side + '"><div class="tl-body"><div class="tl-entry"><div class="tl-time" contenteditable="true">March 20th, 1962</div><div class="tl-icon btn-icon-round btn-icon-lined btn-info"><i class="fa fa-asterisk"></i></div><div class="tl-content"><h4 contenteditable="true" class="tl-tile text-danger">Buy some toys</h4><p contenteditable="true">Ullam, commodi, modi, impedit nostrum odio sit odit necessitatibus accusantium enim voluptates culpa cupiditate cum pariatur a recusandae tenetur aspernatur at beatae.</p></div></div></div></article> ';
                        var elem = $compile(html)($scope);
                        angular.element(document.getElementById('placeTimeline')).append(elem);
                    }

                    ////////////////////////////////////////////////////

                    $scope.fillOutContactInformation = function() {
                        $http.get('../WebApp/rest/getCurrentPlace.php').
                        then(onCurrentPlaceSuccess, onCurrentPlaceError);
                    }

                    var onCurrentPlaceSuccess = function(response) {
                        $scope.placeInfo = response.data[0];
                    }

                    var onCurrentPlaceError = function(response) {
                            $scope.error = "Place could not be fetched!";
                        }
                        ////////////////////////////////////////////////////

                    var onFeedbackListError = function(response) {
                        $scope.error = "Feedback could not be fetched!";
                    }

                    var onFeedbackListSuccess = function(response) {
                        $scope.feedbackList = response.data;
                    }

                    $scope.getFeedbackList = function() {
                            $http.get('../WebApp/rest/getAllFeedback.php').
                            then(onFeedbackListSuccess, onFeedbackListError);
                        }
                        ////////////////////////////////////////////////////

                    $scope.enableGoogleClickListeners = function(map) {
                        //Adds listener that gets coordinates on click
                        google.maps.event.addListener(map, "click", function(event) {
                            var lat = event.latLng.lat();
                            var lng = event.latLng.lng();
                            $scope.placeInfo.Coordinates = lat + "," + lng;
                        });
                    }

                    $scope.loadGoogleMap = function() {

                        var markers = [];
                        var map = new google.maps.Map(document.getElementById('map-canvas'), {
                            mapTypeId: google.maps.MapTypeId.ROADMAP
                        });

                        var defaultBounds = new google.maps.LatLngBounds(
                            new google.maps.LatLng(25.75812, -80.376174),
                            new google.maps.LatLng(25.73812, -80.356174));
                        map.fitBounds(defaultBounds);

                        // Create the search box and link it to the UI element.
                        var input = /** @type {HTMLInputElement} */ (
                            document.getElementById('pac-input'));
                        map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);


                        $scope.enableGoogleClickListeners(map);


                        var searchBox = new google.maps.places.SearchBox(
                            /** @type {HTMLInputElement} */
                            (input));

                        // Listen for the event fired when the user selects an item from the
                        // pick list. Retrieve the matching places for that item.
                        google.maps.event.addListener(searchBox, 'places_changed', function() {
                            var places = searchBox.getPlaces();

                            if (places.length == 0) {
                                return;
                            }
                            for (var i = 0, marker; marker = markers[i]; i++) {
                                marker.setMap(null);
                            }

                            // For each place, get the icon, place name, and location.
                            markers = [];
                            var bounds = new google.maps.LatLngBounds();
                            for (var i = 0, place; place = places[i]; i++) {
                                var image = {
                                    url: place.icon,
                                    size: new google.maps.Size(71, 71),
                                    origin: new google.maps.Point(0, 0),
                                    anchor: new google.maps.Point(17, 34),
                                    scaledSize: new google.maps.Size(25, 25)
                                };

                                // Create a marker for each place.
                                var marker = new google.maps.Marker({
                                    map: map,
                                    icon: image,
                                    title: place.name,
                                    position: place.geometry.location
                                });

                                markers.push(marker);

                                bounds.extend(place.geometry.location);
                            }

                            map.fitBounds(bounds);
                        });

                        // Bias the SearchBox results towards places that are within the bounds of the
                        // current map's viewport.
                        google.maps.event.addListener(map, 'bounds_changed', function() {
                            var bounds = map.getBounds();
                            searchBox.setBounds(bounds);
                        });


                        //$scope.getPlaceDetails();
                    }

                    $scope.setPlaceCoordinates = function(address) {

                        $http({
                                url: 'http://ha-dev.cis.fiu.edu/WebApp/rest/getPlaceCoordinatesFromAddress.php',
                                method: "POST",
                                data: {
                                    'address': address
                                }
                            })
                            .then(function(response) {
                                    // success 
                                    if (response.data.results[0] != null) {
                                        var location = response.data.results[0].geometry.location;
                                        $scope.placeInfo.Coordinates = location.lat + "," + location.lng;
                                    }
                                },
                                function(response) {
                                    // failed  
                                }
                            );
                    }

                    $scope.getPlaceDetails = function() {

                        var url = 'https://maps.googleapis.com/maps/api/place/details/json?placeid=' + $scope.currentPlace.googlePlaceId + '&key=AIzaSyBmLVfCobgTODZrH5pHC9sxTaUEQ0tMVZg&sensor=false';

                        $http.jsonp(url).then(function(response) {
                            $scope.currentPlace.googleResponse = JSON.stringify(response)
                        }, null)
                    }

                    function callback(results, status) {
                        if (status == google.maps.places.PlacesServiceStatus.OK) {
                            for (var i = 0; i < results.length; i++) {
                                createMarker(results[i]);
                            }
                        }
                    }

                    function createMarker(place) {
                        var marker = new google.maps.Marker({
                            map: map,
                            position: place.geometry.location
                        });

                        google.maps.event.addListener(marker, 'click', function() {
                            infowindow.setContent(place.name);
                            infowindow.open(map, this);
                        });
                    }


                    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
 
                    $scope.placeInfo = {
                        PlaceId: "",
                        Name: "",
                        Address: "",
                        Email: "",
                        Phone: "",
                        Hours: "",
                        Coordinates: "",
                        Website: "",
                        Description: "",
                        AudioPaths: "",
                        ImagePaths: "",
                        DocumentPaths: ""
                    }

                    $scope.SavePlace = function() {

                        //$http.defaults.headers.post["Content-Type"] = "application/x-www-form-urlencoded";

                        //UPDATE
                        /*if ($rootScope.getCurrentStep() != 1) {

                            $http({
                                    url: 'http://ha-dev.cis.fiu.edu/WebApp/rest/updateplace.php',
                                    method: "POST",
                                    data: {
                                        'id': $scope.placeInfo.PlaceId,
                                        'name': $scope.placeInfo.Name,
                                        'address': $scope.placeInfo.Address,
                                        'email': $scope.placeInfo.Email,
                                        'phone': $scope.placeInfo.Phone,
                                        'hours': $scope.placeInfo.Hours,
                                        'coordinates': $scope.placeInfo.Coordinates,
                                        'website': $scope.placeInfo.Website,
                                        'description': $scope.placeInfo.Description,
                                        'audioPaths': $scope.placeInfo.AudioPaths,
                                        'imagePaths': $scope.placeInfo.ImagePaths,
                                        'documentPaths': $scope.placeInfo.DocumentPaths
                                    }
                                })
                                .then(function(response) {
                                        // success 
                                    },
                                    function(response) {
                                        // failed  
                                    }
                                );
                        } else //CREATE NEW
                        {
                        */

                        $scope.placeInfo = wizzardService.getCurrentPlace();

                        if($rootScope.getCurrentStep() == 3)
                        {
                            $http({
                                url: 'http://ha-dev.cis.fiu.edu/WebApp/rest/insertplace.php',
                                method: "POST",
                                data: {
                                    'name': $scope.placeInfo.Name,
                                    'address': $scope.placeInfo.Address,
                                    'email': $scope.placeInfo.Email,
                                    'phone': $scope.placeInfo.Phone,
                                    'hours': $scope.placeInfo.Hours,
                                    'coordinates': $scope.placeInfo.Coordinates,
                                    'website': $scope.placeInfo.Website,
                                    'description': $scope.placeInfo.Description,
                                    'audioPaths': $scope.placeInfo.AudioPaths,
                                    'imagePaths': $scope.placeInfo.ImagePaths,
                                    'documentPaths': $scope.placeInfo.DocumentPaths
                                }
                            })
                            .then(function(response) {
                                    // success  
                                },
                                function(response) {
                                    // failed 
                                }
                            );
                        }
                        //}
                    }

                    $rootScope.getCurrentStep = function() {
                        return parseInt($location.path().split('/places/step')[1]);
                    }

                    $rootScope.getCurrentPage = function() {
                        return $location.path().split('/')[$location.path().split('/').length-1];
                    }

                    $scope.SaveStep = function() {

                        var currentStep = $rootScope.getCurrentStep();

                        if (currentStep == 1) {

                            $scope.placeInfo.Name =    $("div[class=gm-title]").eq(0).text();
                            $scope.placeInfo.Address = $("div[class=gm-addr]").eq(0).text();
                            $scope.placeInfo.Website = $("div[class=gm-website]").eq(0).text();
                            $scope.placeInfo.Phone =   $("div[class=gm-phone]").eq(0).text();
                            $scope.setPlaceCoordinates($scope.placeInfo.Address);

                            if ($scope.placeInfo.Name == "" || $scope.placeInfo.Address == "") {
                                $("#selectPlaceErrorMssg").show();
                                $("#selectPlaceErrorMssg").fadeOut(4000);
                                return;
                            }

                            wizzardService.setCurrentPlace($scope.placeInfo);

                        } 
                        else if (currentStep == 2){
                        }
                        else if (currentStep == 3) {
                            $scope.SavePlace();
                        }

                        //Routing
                        $location.path('/places/step' + (currentStep == 3 ? 0 : currentStep + 1));
                    }

                    $scope.Back = function() {
                            $location.path('/places/step' + ($rootScope.getCurrentStep() - 1));
                        }
                        ////////////////////////////////////////////////////////////////////////////////////////////////////////////

                    $scope.main = {
                            brand: "History Explorer",
                        },
                        $scope.currentUser = {
                            name: "Tracy Beeson",
                            username: "",
                            password: ""
                        },
                        $scope.mail = {
                            unread: ""
                        },
                        $scope.page = {
                            current: "this is the current page"
                        },
                        $scope.user = {
                            username: "",
                            password: ""
                        }
                    $scope.dash = {
                        placesCreatedTodayNo: ""
                    }, $scope.places = {
                        showWizzardControls: "hidden",
                        wizzardControlNext: "",
                        wizzardControlBack: "",
                        wizzardControlAdd: "",
                        wizzardNextFA: "",
                        wizzardNextText: ""
                    }, $scope.currentPlace = {
                        googlePlaceId: "ChIJN1t_tDeuEmsRUsoyG83frY4",
                        googleResponse: ""
                    }, $scope.timeline = {
                        side: ""
                    }, $scope.pageTransitionOpts = [{
                        name: "Scale up",
                        "class": "ainmate-scale-up"
                    }, {
                        name: "Fade up",
                        "class": "animate-fade-up"
                    }, {
                        name: "Slide in from right",
                        "class": "ainmate-slide-in-right"
                    }, {
                        name: "Flip Y",
                        "class": "animate-flip-y"
                    }], $scope.admin = {
                        layout: "wide",
                        menu: "horizontal",
                        fixedHeader: !0,
                        fixedSidebar: !1,
                        pageTransition: $scope.pageTransitionOpts[0]
                    }, $scope.color = {
                        primary: "#248AAF",
                        success: "#3CBC8D",
                        info: "#29B7D3",
                        infoAlt: "#666699",
                        warning: "#FAC552",
                        danger: "#E9422E"
                    }
                }
        ])
        .controller("Step1Ctrl", ["$scope", "wizzardService",
                function($scope, wizzardService) {
                    
                    $scope.placeInfo = { 
                        PlaceId: "",
                        Name:     "", //$("div[class=gm-title]").eq(0).text(),
                        Address : "", //$("div[class=gm-addr]").eq(0).text(),
                        Website : "", //$("div[class=gm-website]").eq(0).text(),
                        Phone:    "", //$("div[class=gm-phone]").eq(0).text(),
                        Email: "",
                        Hours: "",
                        Coordinates: "",
                        Description: "",
                        AudioPaths: "",
                        ImagePaths: "",
                        DocumentPaths: ""}

                    wizzardService.setCurrentPlace($scope.placeInfo);
                }
        ])
        .controller("Step2Ctrl", ["$scope", "wizzardService",
                function($scope, wizzardService) {
                    $scope.placeInfo = wizzardService.getCurrentPlace();
                    wizzardService.setCurrentPlace($scope.placeInfo);
                }
        ])
        .controller("Step3Ctrl", ["$scope", 
                function($scope) {
                    $scope.placeInfo = wizzardService.getCurrentPlace();
                    wizzardService.setCurrentPlace($scope.placeInfo);
                }
        ])
        .controller("FeedbackCtrl", ["$scope", "$rootScope", "$http", "$location",
                function($scope, $rootScope, $http, $location) {

                    $scope.author = "";
                    $scope.email = "";
                    $scope.title = "";
                    $scope.feedback = "";

                    $scope.ClearForm = function() {
                        $(".feedbackValidation").val("");
                    }

                    $scope.SubmitFeedback = function() {

                        $http.defaults.headers.post["Content-Type"] = "application/x-www-form-urlencoded";

                        if ($scope.author == "" || $scope.email == "" || $scope.title == "" || $scope.feedback == "")
                            return;

                        $http({
                                url: 'http://ha-dev.cis.fiu.edu/WebApp/rest/insertfeedback.php',
                                method: "POST",
                                data: {
                                    'author': $scope.author,
                                    'email': $scope.email,
                                    'title': $scope.title,
                                    'feedback': $scope.feedback
                                }
                            })
                            .then(function(response) {
                                    // success
                                    $scope.ClearForm();
                                    $location.path('/layout/thankyou');
                                },
                                function(response) {
                                    // failed 
                                }
                            );
                    }
                }
        ])
        .controller("MapCtrl", ["$scope", "$rootScope", "$http", "$location",
                function($scope, $rootScope, $http, $location) {

                }
        ])
        .controller("PlacesCtrl", ["$scope", "$rootScope", "$http", "$location",function($scope, $rootScope, $http, $location) {}
        ])
        .controller('ModalCtrl',
                function($scope, $rootScope, $modal, $http, $location) {

                    $scope.ModalInstanceCtrl = function($scope, $modalInstance, $modal, item) {

                    var currentPage = $rootScope.getCurrentPage();

                        //Getting correct data for the modal based on the page
                        $http({
                            url: 'http://ha-dev.cis.fiu.edu/WebApp/rest/getModalData.php',
                            method: "POST",
                            data: {
                                'currentPage': currentPage
                            }
                        })
                        .then(function(response) {
                                // success 
                                $scope.title = response.data[0].Title;
                                $scope.body =  response.data[0].Body;
                            },
                            function(response) {
                                // failed 
                            }
                        );

                        $scope.item = item;
                        $scope.name = "";

                        $scope.ok = function() {
                            $modalInstance.close();
                        };
                        $scope.cancel = function() {
                            $modalInstance.dismiss('cancel');
                        };
                    }

                    $scope.showModal = function() {
                        $scope.opts = {
                            backdrop: true,
                            backdropClick: true,
                            dialogFade: false,
                            keyboard: true,
                            templateUrl: 'http://ha-dev.cis.fiu.edu/WebApp/views/layout/modal.html',
                            controller: $scope.ModalInstanceCtrl,
                            resolve: {} // empty storage
                        };


                        $scope.opts.resolve.item = function() {
                            return angular.copy({
                                name: $scope.modal
                            }); // pass name to Dialog
                        }

                        var modalInstance = $modal.open($scope.opts);

                        modalInstance.result.then(function() {
                            //on ok button press 
                        }, function() {
                            //on cancel button press 
                        });
                    };
        })
        .controller("DashboardCtrl", ["$scope",function($scope) {}
        ])
        .controller("HeaderCtrl", ["$scope",function() {}
        ])
        .controller("NavContainerCtrl", ["$scope",function() {}
        ])
        .controller("NavCtrl", ["$scope", "taskStorage", "filterFilter",
                function($scope, taskStorage, filterFilter) {
                    var tasks;
                    return tasks = $scope.tasks = taskStorage.get(), $scope.taskRemainingCount = filterFilter(tasks, {
                        completed: !1
                    }).length, $scope.$on("taskRemaining:changed", function(event, count) {
                        return $scope.taskRemainingCount = count
                    })
                }
        ])
        .controller("flotChartCtrl", ["$scope",
                function($scope) {
                    var areaChart, barChart, barChartH, lineChart1;
                    return lineChart1 = {}, lineChart1.data1 = [
                        [1, 15],
                        [2, 20],
                        [3, 14],
                        [4, 10],
                        [5, 10],
                        [6, 20],
                        [7, 28],
                        [8, 26],
                        [9, 22]
                    ], $scope.line1 = {}, $scope.line1.data = [{
                        data: lineChart1.data1,
                        label: "Trend"
                    }], $scope.line1.options = {
                        series: {
                            lines: {
                                show: !0,
                                fill: !0,
                                fillColor: {
                                    colors: [{
                                        opacity: 0
                                    }, {
                                        opacity: .3
                                    }]
                                }
                            },
                            points: {
                                show: !0,
                                lineWidth: 2,
                                fill: !0,
                                fillColor: "#ffffff",
                                symbol: "circle",
                                radius: 5
                            }
                        },
                        colors: [$scope.color.primary, $scope.color.infoAlt],
                        tooltip: !0,
                        tooltipOpts: {
                            defaultTheme: !1
                        },
                        grid: {
                            hoverable: !0,
                            clickable: !0,
                            tickColor: "#f9f9f9",
                            borderWidth: 1,
                            borderColor: "#eeeeee"
                        },
                        xaxis: {
                            ticks: [
                                [1, "Jan."],
                                [2, "Feb."],
                                [3, "Mar."],
                                [4, "Apr."],
                                [5, "May"],
                                [6, "June"],
                                [7, "July"],
                                [8, "Aug."],
                                [9, "Sept."],
                                [10, "Oct."],
                                [11, "Nov."],
                                [12, "Dec."]
                            ]
                        }
                    }, areaChart = {}, areaChart.data1 = [
                        [2007, 15],
                        [2008, 20],
                        [2009, 10],
                        [2010, 5],
                        [2011, 5],
                        [2012, 20],
                        [2013, 28]
                    ], areaChart.data2 = [
                        [2007, 15],
                        [2008, 16],
                        [2009, 22],
                        [2010, 14],
                        [2011, 12],
                        [2012, 19],
                        [2013, 22]
                    ], $scope.area = {}, $scope.area.data = [{
                        data: areaChart.data1,
                        label: "Value A",
                        lines: {
                            fill: !0
                        }
                    }, {
                        data: areaChart.data2,
                        label: "Value B",
                        points: {
                            show: !0
                        },
                        yaxis: 2
                    }], $scope.area.options = {
                        series: {
                            lines: {
                                show: !0,
                                fill: !1
                            },
                            points: {
                                show: !0,
                                lineWidth: 2,
                                fill: !0,
                                fillColor: "#ffffff",
                                symbol: "circle",
                                radius: 5
                            },
                            shadowSize: 0
                        },
                        grid: {
                            hoverable: !0,
                            clickable: !0,
                            tickColor: "#f9f9f9",
                            borderWidth: 1,
                            borderColor: "#eeeeee"
                        },
                        colors: [$scope.color.success, $scope.color.danger],
                        tooltip: !0,
                        tooltipOpts: {
                            defaultTheme: !1
                        },
                        xaxis: {
                            mode: "time"
                        },
                        yaxes: [{}, {
                            position: "right"
                        }]
                    }, barChart = {}, barChart.data1 = [
                        [2008, 20],
                        [2009, 10],
                        [2010, 5],
                        [2011, 5],
                        [2012, 20],
                        [2013, 28]
                    ], barChart.data2 = [
                        [2008, 16],
                        [2009, 22],
                        [2010, 14],
                        [2011, 12],
                        [2012, 19],
                        [2013, 22]
                    ], barChart.data3 = [
                        [2008, 12],
                        [2009, 30],
                        [2010, 20],
                        [2011, 19],
                        [2012, 13],
                        [2013, 20]
                    ], $scope.barChart = {}, $scope.barChart.data = [{
                        label: "Value A",
                        data: barChart.data1
                    }, {
                        label: "Value B",
                        data: barChart.data2
                    }, {
                        label: "Value C",
                        data: barChart.data3
                    }], $scope.barChart.options = {
                        series: {
                            stack: !0,
                            bars: {
                                show: !0,
                                fill: 1,
                                barWidth: .3,
                                align: "center",
                                horizontal: !1,
                                order: 1
                            }
                        },
                        grid: {
                            hoverable: !0,
                            borderWidth: 1,
                            borderColor: "#eeeeee"
                        },
                        tooltip: !0,
                        tooltipOpts: {
                            defaultTheme: !1
                        },
                        colors: [$scope.color.success, $scope.color.info, $scope.color.warning, $scope.color.danger]
                    }, $scope.barChart1 = {}, $scope.barChart1.data = [{
                        label: "Value A",
                        data: barChart.data1,
                        bars: {
                            order: 0
                        }
                    }, {
                        label: "Value B",
                        data: barChart.data2,
                        bars: {
                            order: 1
                        }
                    }, {
                        label: "Value C",
                        data: barChart.data3,
                        bars: {
                            order: 2
                        }
                    }], $scope.barChart1.options = {
                        series: {
                            stack: !0,
                            bars: {
                                show: !0,
                                fill: 1,
                                barWidth: .2,
                                align: "center",
                                horizontal: !1
                            }
                        },
                        grid: {
                            hoverable: !0,
                            borderWidth: 1,
                            borderColor: "#eeeeee"
                        },
                        tooltip: !0,
                        tooltipOpts: {
                            defaultTheme: !1
                        },
                        colors: [$scope.color.success, $scope.color.info, $scope.color.warning, $scope.color.danger]
                    }, barChartH = {}, barChartH.data1 = [
                        [85, 10],
                        [50, 20],
                        [55, 30]
                    ], barChartH.data2 = [
                        [77, 10],
                        [60, 20],
                        [70, 30]
                    ], barChartH.data3 = [
                        [100, 10],
                        [70, 20],
                        [55, 30]
                    ], $scope.barChart2 = {}, $scope.barChart2.data = [{
                        label: "Value A",
                        data: barChartH.data1,
                        bars: {
                            order: 1
                        }
                    }, {
                        label: "Value B",
                        data: barChartH.data2,
                        bars: {
                            order: 2
                        }
                    }, {
                        label: "Value C",
                        data: barChartH.data3,
                        bars: {
                            order: 3
                        }
                    }], $scope.barChart2.options = {
                        series: {
                            stack: !0,
                            bars: {
                                show: !0,
                                fill: 1,
                                barWidth: 1,
                                align: "center",
                                horizontal: !0
                            }
                        },
                        grid: {
                            hoverable: !0,
                            borderWidth: 1,
                            borderColor: "#eeeeee"
                        },
                        tooltip: !0,
                        tooltipOpts: {
                            defaultTheme: !1
                        },
                        colors: [$scope.color.success, $scope.color.info, $scope.color.warning, $scope.color.danger]
                    }, $scope.pieChart = {}, $scope.pieChart.data = [{
                        label: "Download Sales",
                        data: 12
                    }, {
                        label: "In-Store Sales",
                        data: 30
                    }, {
                        label: "Mail-Order Sales",
                        data: 20
                    }, {
                        label: "Online Sales",
                        data: 19
                    }], $scope.pieChart.options = {
                        series: {
                            pie: {
                                show: !0
                            }
                        },
                        legend: {
                            show: !0
                        },
                        grid: {
                            hoverable: !0,
                            clickable: !0
                        },
                        colors: [$scope.color.primary, $scope.color.success, $scope.color.info, $scope.color.warning, $scope.color.danger],
                        tooltip: !0,
                        tooltipOpts: {
                            content: "%p.0%, %s",
                            defaultTheme: !1
                        }
                    }, $scope.donutChart = {}, $scope.donutChart.data = [{
                        label: "Download Sales",
                        data: 12
                    }, {
                        label: "In-Store Sales",
                        data: 30
                    }, {
                        label: "Mail-Order Sales",
                        data: 20
                    }, {
                        label: "Online Sales",
                        data: 19
                    }], $scope.donutChart.options = {
                        series: {
                            pie: {
                                show: !0,
                                innerRadius: .5
                            }
                        },
                        legend: {
                            show: !0
                        },
                        grid: {
                            hoverable: !0,
                            clickable: !0
                        },
                        colors: [$scope.color.primary, $scope.color.success, $scope.color.info, $scope.color.warning, $scope.color.danger],
                        tooltip: !0,
                        tooltipOpts: {
                            content: "%p.0%, %s",
                            defaultTheme: !1
                        }
                    }, $scope.donutChart2 = {}, $scope.donutChart2.data = [{
                        label: "Download Sales",
                        data: 12
                    }, {
                        label: "In-Store Sales",
                        data: 30
                    }, {
                        label: "Mail-Order Sales",
                        data: 20
                    }, {
                        label: "Online Sales",
                        data: 19
                    }, {
                        label: "Direct Sales",
                        data: 15
                    }], $scope.donutChart2.options = {
                        series: {
                            pie: {
                                show: !0,
                                innerRadius: .45
                            }
                        },
                        legend: {
                            show: !1
                        },
                        grid: {
                            hoverable: !0,
                            clickable: !0
                        },
                        colors: ["#1BB7A0", "#39B5B9", "#52A3BB", "#619CC4", "#6D90C5"],
                        tooltip: !0,
                        tooltipOpts: {
                            content: "%p.0%, %s",
                            defaultTheme: !1
                        }
                    }
                }
        ])
    }
    .call(this)
);