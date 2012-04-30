/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 * Broadleaf Commerce REST API Test Client
 *
 * @author Elbert Bautista
 *
 */

if (typeof jQuery !== 'undefined') {(function($) {
    var broadleaf = broadleaf || {
        contextPath : "broadleafdemo",
        customerId : ""
    };

    var notifications = [];

    $.fn.extractObject = function() {
        var accum = {};
        function add(accum, namev, value) {
            if (namev.length == 1)
                accum[namev[0]] = value;
            else {
                if (accum[namev[0]] == null)
                    accum[namev[0]] = {};
                add(accum[namev[0]], namev.slice(1), value);
            }
        };
        this.find('input, textarea, select').each(function() {
            if ($(this).prop('checked') && ('checked' == $(this).prop('checked'))) {
                $(this).val(true);
            }
            add(accum, $(this).attr('name').split('.'), $(this).val());
        });
        return accum;
    };

	$(document).ready(function() {
		console.log('initializing application...');

        $(function(){
            $('form.uniForm').uniform();
        });

		$('#spinner').ajaxStart(function() {
			$(this).fadeIn();
		}).ajaxStop(function() {
			$(this).fadeOut();
		});

        $('#prev').click(function(){
            var offset = $(this).data('offset');
            if (offset >= 0) {
                fetchProducts(20, offset);
            }
            return false;
        });

        $('#next').click(function(){
            var offset = $(this).data('offset');
            fetchProducts(20, offset);
            return false;
        });

        $('#about').click(function(){
            $.modal("<div id='about_modal'></div>");
            var data = {};
            $('#about_modal').mustache('about-template', data);
            return false;
        });

        $("ul#navigation li a").click(function() {
            $("ul#navigation li").removeClass("selected");
            $(this).parents().addClass("selected");

            if ($(this).parents().hasClass("one")){
                showCatalog();
            }

            if ($(this).parents().hasClass("two")){
                showCart();
            }

            if ($(this).parents().hasClass("three")){
                showFulfillment();
            }

            if ($(this).parents().hasClass("four")){
                showCheckout();
            }

            if ($(this).parents().hasClass("five")){
                showOrderHistory();
            }

            return false;
        });

        $('#debug_console_toggle').click(function() {
            $('#debug_list').slideToggle();
        });

        $('.json_output').live("click", function( event ) {
            var idx = $(this).data('idx');
            $.modal(prettyPrint(notifications[idx].json, {maxArray: 20, maxDepth: 10}), {minWidth:800, minHeight:500, opacity:80, overlayCss: {backgroundColor:"#fff"}});
        });

        $('.product_detail').live("click", function( event ) {
            $.modal("<div id='product_details' class='modal_overlay'><div id='product_info'></div><div id='skus'></div></div>", {modal:false});
            var id = $(this).data('id');
            fetchProduct(id);
            fetchSkus(id);
        });

        $('.add_to_cart').live("click", function(event) {
            if (broadleaf.customerId != null){
                var categoryId = $(this).data('categoryid');
                var productId = $(this).data('productid');
                var skuId = $(this).data('skuid');
                addToCart(categoryId, productId, skuId);
            }
        });

        $('.view_cart').live("click", function(event) {
            if (broadleaf.customerId != null){
                fetchCart();
                showCart();
            }
        });

        $('.view_fulfillment').live("click", function(event) {
            $('#fulfillmentGroups').empty();
            fetchFulfillmentGroupsForOrder();
        });

        $('.delete_fulfillment').live("click", function(event) {
            removeAllFulfillmentGroupsFromOrder();
        });

        $('#fulfillmentGroupWrapper_submit').click(function() {
            var form = $('#fulfillmentGroupWrapper_form').extractObject();
            addFulfillmentGroup(form);
        });

        $('#fulfillmentGroupItem_submit').click(function() {
            var form = $('#fulfillmentGroupItem_form').extractObject();
            var id = $('#fulfillmentGroupId').val();
            addFulfillmentGroupItem(form, id);
        });

        $('#checkout_submit').click(function() {
            var form = $('#checkout_form').extractObject();
            performCheckout(form);
        });

        $('#order_history_submit').click(function() {
           fetchOrderHistory();
        });

        console.log('initializing templates...');
        $.Mustache.load('./templates/about-template.htm');
		$.Mustache.load('./templates/product-template.htm');
        $.Mustache.load('./templates/product-detail-template.htm');
        $.Mustache.load('./templates/category-template.htm');
        $.Mustache.load('./templates/sku-template.htm');
        $.Mustache.load('./templates/cart-template.htm');
        $.Mustache.load('./templates/cart-mini-template.htm');
        $.Mustache.load('./templates/fulfillment-template.htm');
        $.Mustache.load('./templates/orders-template.htm');

        console.log('loading data...');
        createCart();
        fetchAllCategories();
        fetchProducts(20, 0);

	});

    function showCatalog() {
        $('#tab-one').show();
        $('#tab-two').hide();
        $('#tab-three').hide();
        $('#tab-four').hide();
        $('#tab-five').hide();
    }

    function showCart() {
        $("ul#navigation li").removeClass("selected");
        $('.two').addClass("selected");
        $('#tab-one').hide();
        $('#tab-two').show();
        $('#tab-three').hide();
        $('#tab-four').hide();
        $('#tab-five').hide();
    }

    function showFulfillment()  {
        $('#tab-one').hide();
        $('#tab-two').hide();
        $('#tab-three').show();
        $('#tab-four').hide();
        $('#tab-five').hide();
    }

    function showCheckout()  {
        $('#tab-one').hide();
        $('#tab-two').hide();
        $('#tab-three').hide();
        $('#tab-four').show();
        $('#tab-five').hide();
    }

    function showOrderHistory() {
        $('#tab-one').hide();
        $('#tab-two').hide();
        $('#tab-three').hide();
        $('#tab-four').hide();
        $('#tab-five').show();
    }

    function fetchProducts(limit, offset) {
        var uri = "/" + broadleaf.contextPath + "/api/catalog/products?limit=" + limit + "&offset=" + offset;

        $.ajax({ url: uri,
            type: 'GET',
            datatype: 'json',
            success: function(data) {
                $('#products').mustache('product-template', data);
                $('#prev').data('offset', offset-20);
                $('#next').data('offset', offset+20);
                debug("GET: " + uri, data);
            },
            error: function() {
                alert('error!');
            }
        });
    }

    function fetchProduct(id){
        var uri = "/" + broadleaf.contextPath + "/api/catalog/product/" + id;
        $.ajax({ url: uri,
            type: 'GET',
            datatype: 'json',
            success: function(data) {
                $('#product_info').mustache('product-detail-template', data);
                debug("GET: " + uri, data);
            },
            error: function() {
                alert('error!');
            }
        });
    }

    function fetchSkus(id){
        var uri = "/" + broadleaf.contextPath + "/api/catalog/product/" + id + "/skus";
        $.ajax({ url: uri,
            type: 'GET',
            datatype: 'json',
            success: function(data) {
                //Dynamically adding category and product id to the model for add to cart;
                data.categoryId = 1;
                data.productId = id;
                $('#skus').mustache('sku-template', data);
                debug("GET: " + uri, data);
            },
            error: function() {
                alert('error!');
            }
        });
    }

    function fetchAllCategories() {
        var uri = "/" + broadleaf.contextPath + "/api/catalog/categories";
        $.ajax({ url: uri,
            type: 'GET',
            datatype: 'json',
            success: function(data) {
                $('#categories').mustache('category-template', data);
                debug("GET: " + uri, data);
            },
            error: function() {
                alert('error!');
            }
        });
    }

    function fetchCart() {
        var uri = "/" + broadleaf.contextPath + "/api/cart?customerId=" + broadleaf.customerId;
        if (broadleaf.customerId != null) {
            $.ajax({ url: uri,
                type: 'GET',
                datatype: 'json',
                success: function(data) {
                    $('#mini_cart').mustache('cart-mini-template', data);
                    $('#cart').mustache('cart-template', data);
                    $('.orderId').val(data.id);
                    debug("GET: " + uri, data);
                }
            });
        }
    }

    function createCart() {
        var uri = "/" + broadleaf.contextPath + "/api/cart";
        $.ajax({ url: uri,
            type: 'POST',
            datatype: 'json',
            success: function(data) {
                broadleaf.customerId = data.customer.id;
                debug("POST: " + uri, data);
                fetchCart();
            }
        });
    }

    function addToCart(categoryId, productId, skuId) {
        var uri = "/" + broadleaf.contextPath + "/api/cart/"+categoryId+"/"+productId+"/"+skuId+"?customerId=" + broadleaf.customerId;
        $.ajax({ url: uri,
            type: 'POST',
            datatype: 'json',
            success: function(data) {
                $('#mini_cart').mustache('cart-mini-template', data);
                $('#cart').mustache('cart-template', data);
                debug("POST: " + uri, data);
                $.modal.close();
            }
        });
    }

    function addFulfillmentGroup(form) {
        //alert("POST:" + JSON.stringify(form));
        var uri = "/" + broadleaf.contextPath + "/api/cart/fulfillment/group" + "?customerId=" + broadleaf.customerId;
        $.ajax({ url: uri,
            'data': JSON.stringify(form),
            'type': 'POST',
            'processData': false,
            'contentType': 'application/json',
            success: function(data) {
                debug("POST: " + uri, data);
                alert('added!');
            },
            error: function(xhr) {
                alert('error! Status: ' + xhr.status);
            }
        });
    }

    function addFulfillmentGroupItem(form, id) {
        //alert("POST:" + JSON.stringify(form));
        var uri = "/" + broadleaf.contextPath + "/api/cart/fulfillment/group/" + id + "?customerId=" + broadleaf.customerId;
        $.ajax({ url: uri,
            'data': JSON.stringify(form),
            'type': 'PUT',
            'processData': false,
            'contentType': 'application/json',
            success: function(data) {
                debug("PUT: " + uri, data);
                alert('added!');
            },
            error: function(xhr) {
                alert('error! Status: ' + xhr.status);
            }
        });
    }

    function fetchFulfillmentGroupsForOrder() {
        var uri = "/" + broadleaf.contextPath + "/api/cart/fulfillment/groups" + "?customerId=" + broadleaf.customerId;
        $.ajax({ url: uri,
            type: 'GET',
            datatype: 'json',
            success: function(data) {
                $('#fulfillmentGroups').mustache('fulfillment-template', data);
                debug("GET: " + uri, data);
            },
            error: function(xhr) {
                alert('error! Status: ' + xhr.status);
            }
        });
    }

    function removeAllFulfillmentGroupsFromOrder() {
        var uri =  "/" + broadleaf.contextPath + "/api/cart/fulfillment/groups" + "?customerId=" + broadleaf.customerId;
        $.ajax({ url: uri,
            type: 'DELETE',
            datatype: 'json',
            success: function(data) {
                alert('deleted!');
                $('#fulfillmentGroups').empty();
                debug("GET: " + uri, data);
            },
            error: function(xhr) {
                alert('error! Status: ' + xhr.status);
            }
        });
    }

    function performCheckout(form) {
        //alert("POST:" + JSON.stringify(form));
        var uri = "/" + broadleaf.contextPath + "/api/cart/checkout" + "?customerId=" + broadleaf.customerId;
        $.ajax({ url: uri,
            'data': JSON.stringify(form),
            'type': 'POST',
            'processData': false,
            'contentType': 'application/json',
            success: function(data) {
                alert('success!');
                debug("POST: " + uri, data);
            },
            error: function(xhr) {
                alert('error! Status: ' + xhr.status);
            }
        });
    }

    function fetchOrderHistory() {
        var uri = "/" + broadleaf.contextPath + "/api/orders" + "?customerId=" + broadleaf.customerId;
        $.ajax({ url: uri,
            type: 'GET',
            datatype: 'json',
            success: function(data) {
                $('#orders').mustache('orders-template', data);
                debug("GET: " + uri, data);
            },
            error: function(xhr) {
                alert('error! Status: ' + xhr.status);
            }
        });
    }

    function debug(notification, data) {
        if (notifications.length >= 5) {
            notifications.shift();
        }

        var debug = {
            name : notification,
            json : data
        };

        notifications.push(debug);

        $('#debug_list').empty();
        for (var i=0; i<notifications.length; i++) {
            $('#debug_list').append("<div class='debug_notification'>" + notifications[i].name + "&nbsp;<a href='#' data-idx='" + i + "' class='json_output'> View JSON</a></div>");
        }
    }

		
})(jQuery);
}
