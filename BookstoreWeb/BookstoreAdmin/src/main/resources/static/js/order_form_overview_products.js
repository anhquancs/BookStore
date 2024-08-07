var fieldProductCost;
var fieldSubtotal;
var fieldShippingCost;
var fieldTax;
var fieldTotal;
var initialShippingCost = 0.0;

$(document).ready(function() {
    fieldProductCost = $("#productCost");
    fieldSubtotal = $("#subtotal");
    fieldShippingCost = $("#shippingCost");
    fieldTax = $("#tax");
    fieldTotal = $("#total");
    
    formatOrderAmounts();
    formatProductAmounts();
    
    // Lưu chi phí vận chuyển ban đầu
    initialShippingCost = getNumberValueRemovedThousandSeparator(fieldShippingCost);
    
    $("#productList").on("change", ".quantity-input", function(e) {
        updateSubtotalWhenQuantityChanged($(this));
        updateOrderAmounts();
    });
    
    $("#productList").on("change", ".price-input", function(e) {
        updateSubtotalWhenPriceChanged($(this));
        updateOrderAmounts();
    });
    
    $("#productList").on("change", ".cost-input", function(e) {
        updateOrderAmounts();
    });
    
    // Không cần cập nhật khi chi phí vận chuyển thay đổi
    // $("#productList").on("change", ".ship-input", function(e) {
    //     updateOrderAmounts();
    // });         
});

function updateOrderAmounts() {
    totalCost = 0.0;
    
    $(".cost-input").each(function(e) {
        costInputField = $(this);
        rowNumber = costInputField.attr("rowNumber");
        quantityValue = $("#quantity" + rowNumber).val();
        
        productCost = getNumberValueRemovedThousandSeparator(costInputField); 
        totalCost += productCost * parseInt(quantityValue); 
    });
    
    setAndFormatNumberForField("productCost", totalCost);
    
    orderSubtotal = 0.0;
    
    $(".subtotal-output").each(function(e) {
        productSubtotal = getNumberValueRemovedThousandSeparator($(this));
        orderSubtotal += productSubtotal;
    });
    
    setAndFormatNumberForField("subtotal", orderSubtotal);
    
    // Sử dụng chi phí vận chuyển ban đầu
    shippingCost = initialShippingCost;
    
    setAndFormatNumberForField("shippingCost", shippingCost);
    
    tax = getNumberValueRemovedThousandSeparator(fieldTax);
    orderTotal = orderSubtotal + tax + shippingCost;
    setAndFormatNumberForField("total", orderTotal);
}

function setAndFormatNumberForField(fieldId, fieldValue) {
    formattedValue = $.number(fieldValue);
    $("#" + fieldId).val(formattedValue);
}

function getNumberValueRemovedThousandSeparator(fieldRef) {
    let fieldValue = fieldRef.val();

    fieldValue = fieldValue.replace(/\./g, '');
    fieldValue = fieldValue.replace(/,/g, '.');

    return parseFloat(fieldValue);
}

function updateSubtotalWhenPriceChanged(input) {
    priceValue = getNumberValueRemovedThousandSeparator(input);
    rowNumber = input.attr("rowNumber");
    
    quantityField = $("#quantity" + rowNumber);
    quantityValue = quantityField.val();
    newSubtotal = parseFloat(quantityValue) * priceValue;
    
    setAndFormatNumberForField("subtotal" + rowNumber, newSubtotal); 
}

function updateSubtotalWhenQuantityChanged(input) {
    quantityValue = input.val();
    rowNumber = input.attr("rowNumber");
    priceValue = getNumberValueRemovedThousandSeparator($("#price" + rowNumber));
    newSubtotal = parseFloat(quantityValue) * priceValue;
    
    setAndFormatNumberForField("subtotal" + rowNumber, newSubtotal);
}

function formatProductAmounts() {
    $(".cost-input").each(function(e) {
        formatNumberForField($(this));
    });

    $(".price-input").each(function(e) {
        formatNumberForField($(this));
    }); 
    
    $(".subtotal-output").each(function(e) {
        formatNumberForField($(this));
    }); 
    
    $(".ship-input").each(function(e) {
        formatNumberForField($(this));
    }); 
}

function formatOrderAmounts() {
    formatNumberForField(fieldProductCost);
    formatNumberForField(fieldSubtotal);
    formatNumberForField(fieldShippingCost);
    formatNumberForField(fieldTax);
    formatNumberForField(fieldTotal); 
}

function formatNumberForField(fieldRef) {
    fieldRef.val($.number(fieldRef.val()));
}


function processFormBeforeSubmit() {
	setCityName();
	
	removeThousandSeparatorForField(fieldProductCost);
	removeThousandSeparatorForField(fieldSubtotal);
	removeThousandSeparatorForField(fieldShippingCost);
	removeThousandSeparatorForField(fieldTax);
	removeThousandSeparatorForField(fieldTotal);
	
	$(".cost-input").each(function(e) {
		removeThousandSeparatorForField($(this));
	});
	
	$(".price-input").each(function(e) {
		removeThousandSeparatorForField($(this));
	});
	
	$(".subtotal-output").each(function(e) {
		removeThousandSeparatorForField($(this));
	});			
	
	$(".ship-input").each(function(e) {
		removeThousandSeparatorForField($(this));
	});		
	
	return true;
}

function removeThousandSeparatorForField(fieldRef) {
	fieldRef.val(fieldRef.val().replace(".", ""));
}

function setCityName() {
	selectedCity = $("#city option:selected");
	cityName = selectedCity.text();
	$("#cityName").val(cityName);
}
