var buttonLoadDistricts;
var dropDownCityDistricts;
var dropDownDistricts;
var buttonAddDistrict;
var buttonUpdateDistrict;
var buttonDeleteDistrict;
var labelDistrictName;
var fieldDistrictName;

$(document).ready(function() {
    buttonLoadDistricts = $("#buttonLoadCitiesForDistricts");
    dropDownCityDistricts = $("#dropDownCitiesForDistricts");
    dropDownDistricts = $("#dropDownDistricts");
    buttonAddDistrict = $("#buttonAddDistrict");
    buttonUpdateDistrict = $("#buttonUpdateDistrict");
    buttonDeleteDistrict = $("#buttonDeleteDistrict");
    labelDistrictName = $("#labelDistrictName");
    fieldDistrictName = $("#fieldDistrictName");

    buttonLoadDistricts.click(function() {
        loadCitiesDistricts();
    });

    dropDownCityDistricts.on("change", function() {
        loadDistrictsCity();
    });

    dropDownDistricts.on("change", function() {
        changeFormDistrictToSelectedDistrict();
    });

    buttonAddDistrict.click(function() {
        if (buttonAddDistrict.val() == "Add") {
            addDistrict();
        } else {
            changeFormDistrictToNew();
        }
    });

    buttonUpdateDistrict.click(function() {
        updateDistrict();
    });

    buttonDeleteDistrict.click(function() {
        deleteDistrict();
    });
});

function deleteDistrict() {
    districtId = dropDownDistricts.val();

    url = contextPath + "districts/delete/" + districtId;

    $.ajax({
        type: 'DELETE',
        url: url,
        beforeSend: function(xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        }
    }).done(function() {
        $("#dropDownDistricts option[value='" + districtId + "']").remove();
        changeFormDistrictToNew();
        showToastMessage("The district has been deleted");
    }).fail(function() {
        showToastMessage("ERROR: Could not connect to server encountered an error");
    });
}

function updateDistrict() {
    url = contextPath + "districts/save";
    districtId = dropDownDistricts.val();
    districtName = fieldDistrictName.val();

    selectedCity = $("#dropDownCitiesForDistricts option:selected");
    cityId = selectedCity.val();
    cityName = selectedCity.text();

    jsonData = {id: districtId, name: districtName, city: {id: cityId, name: cityName}};
    
    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: function(xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: JSON.stringify(jsonData),
        contentType: 'application/json'
    }).done(function(districtId) {
        $("#dropDownDistricts option:selected").text(districtName);
        showToastMessage("The district has been updated");
        changeFormDistrictToNew();
    }).fail(function() {
        showToastMessage("ERROR: Could not connect to server encountered an error");
    });
}

function addDistrict() {
    url = contextPath + "districts/save";
    districtName = fieldDistrictName.val();
    
    jsonData = {name: districtName, city: {id: cityId, name: cityName}};

    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: function(xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: JSON.stringify(jsonData),
        contentType: 'application/json'
    }).done(function(districtId) {
        selectNewlyAddedDistrict(districtId, districtName);
        showToastMessage("The new district has been added");
    }).fail(function() {
        showToastMessage("ERROR: Could not connect to server encountered an error");
    });
}

function selectNewlyAddedDistrict(districtId, districtName) {
    $("<option>").val(districtId).text(districtName).appendTo(dropDownDistricts);

    $("#dropDownDistricts option[value='" + districtId + "']").prop("selected", true);

    fieldDistrictName.val("").focus();
}

function changeFormDistrictToNew() {
    buttonAddDistrict.val("Add");
    labelDistrictName.text("District Name");

    buttonUpdateDistrict.prop("disabled", true);
    buttonDeleteDistrict.prop("disabled", true);

    fieldDistrictName.val("").focus();
}

function changeFormDistrictToSelectedDistrict() {
    buttonAddDistrict.prop("value", "New");
    buttonUpdateDistrict.prop("disabled", false);
    buttonDeleteDistrict.prop("disabled", false);

    labelDistrictName.text("Selected District");

    selectedDistrictName = $("#dropDownDistricts option:selected").text();
    fieldDistrictName.val(selectedDistrictName);
}

function loadDistrictsCity() {
    selectedCity = $("#dropDownCitiesForDistricts option:selected"); 
    cityId= selectedCity.val();
    url= contextPath + "districts/list_by_city/" + cityId;        
    
    $.get(url, function(responseJSON) {
        dropDownDistricts.empty();

        $.each(responseJSON, function(index, district) {
            $("<option>").val(district.id).text(district.name).appendTo(dropDownDistricts);
        });
    }).done(function() {
        changeFormDistrictToNew();
        showToastMessage("All districts have been loaded for city" + selectedCity.text());
    }).fail(function() {
        showToastMessage("ERROR: Could not connect to server or server encountered an error");
    });
}

function loadCitiesDistricts() {
    url = contextPath + "cities/list";
    $.get(url, function(responseJSON) {
        dropDownCityDistricts.empty();

        $.each(responseJSON, function(index, city) {
            $("<option>").val(city.id).text(city.name).appendTo(dropDownCityDistricts);
        });
    }).done(function() {
        buttonLoadDistricts.val("Refresh City List");
        showToastMessage("All cities have been loaded");
    }).fail(function() {
        showToastMessage("ERROR: Could not connect to server encountered an error");
    });
}

function showToastMessage(message) {
    $("#toastMessage").text(message);
    $(".toast").toast('show');
}
