$(document).ready(function() {
    $("#trackList").on("click", ".linkRemoveTrack", function(e) {
        e.preventDefault();
        deleteTrack($(this));
        updateTrackCountNumbers();
    });
    
    $("#track").on("click", "#linkAddTrack", function(e) {
        e.preventDefault();
        addNewTrackRecord();
    });
    
    $("#trackList").on("change", ".dropDownStatus", function(e) {
        const dropDownList = $(this);
        const rowNumber = dropDownList.attr("rowNumber");
        const selectedStatus = dropDownList.val();
        
        const defaultNote = statusDescriptions[selectedStatus] || "";
        $("#trackNote" + rowNumber).val(defaultNote);
    });
});

function deleteTrack(link) {
    const rowNumber = link.attr('rowNumber');
    $("#rowTrack" + rowNumber).remove();
    $("#emptyLine" + rowNumber).remove();    
}

function updateTrackCountNumbers() {
    $(".divCountTrack").each(function (index, element) {
        element.innerHTML = "" + (index + 1);
    });
}

function addNewTrackRecord() {    
    const htmlCode = generateTrackCode();    
    $("#trackList").append(htmlCode);
}

// Define a mapping of statuses to their descriptions
const statusDescriptions = {
    "NEW": "Order was placed by the customer",
    "CANCELLED": "Order was rejected",
    "PROCESSING": "Order is being processed",
    "PACKAGED": "Products were packaged",
    "PICKED": "Shipper picked the package",
    "SHIPPING": "Shipper is delivering the package",
    "DELIVERED": "Customer received products",
    "RETURNED": "Products were returned",
    "RETURN_REQUESTED": "Customer sent request to return purchase",
    "PAID": "Customer has paid this order",
    "REFUNDED": "Customer has been refunded"
};

function generateTrackCode() {
    const nextCount = $(".hiddenTrackId").length + 1;
    const rowId = "rowTrack" + nextCount;
    const emptyLineId = "emptyLine" + nextCount;
    const trackNoteId = "trackNote" + nextCount;
    const currentDateTime = formatCurrentDateTime();
    
    // Set default status for new rows (e.g., "NEW")
    const defaultStatus = "NEW";
    const defaultDescription = statusDescriptions[defaultStatus] || "";

    let htmlCode = `
        <div class="row border rounded p-1" id="${rowId}">
            <input type="hidden" name="trackId" value="0" class="hiddenTrackId" />
            <div class="col-2">
                <div class="divCountTrack">${nextCount}</div>
                <div class="mt-1">
                    <a class="fas fa-trash icon-dark linkRemoveTrack" href="" rowNumber="${nextCount}"></a>
                </div>					
            </div>				
                
            <div class="col-10">
                <div class="form-group row">
                    <label class="col-form-label">Time:</label>
                    <div class="col">
                        <input type="datetime-local" name="trackDate" value="${currentDateTime}" class="form-control" required style="max-width: 300px" />						
                    </div>
                </div>					
                <div class="form-group row">  
                    <label class="col-form-label">Status:</label>
                    <div class="col">
                        <select name="trackStatus" class="form-control dropDownStatus" required style="max-width: 150px" rowNumber="${nextCount}">
                            ${$("#trackStatusOptions").html()}
                        </select>						
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-form-label">Notes:</label>
                    <div class="col">
                        <textarea rows="2" cols="10" class="form-control" name="trackNotes" id="${trackNoteId}" style="max-width: 300px" required>${defaultDescription}</textarea>
                    </div>
                </div>
            </div>				
        </div>	
        <div id="${emptyLineId}" class="row">&nbsp;</div>
    `;
    
    return htmlCode;
}

function formatCurrentDateTime() {
    const date = new Date();
    let year = date.getFullYear();
    let month = date.getMonth() + 1;
    let day = date.getDate();
    let hour = date.getHours();
    let minute = date.getMinutes();
    let second = date.getSeconds();
    
    if (month < 10) month = "0" + month;
    if (day < 10) day = "0" + day;
    
    if (hour < 10) hour = "0" + hour;
    if (minute < 10) minute = "0" + minute;
    if (second < 10) second = "0" + second;
    
    return `${year}-${month}-${day}T${hour}:${minute}:${second}`;
}
