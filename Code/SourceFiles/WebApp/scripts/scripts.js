function AjaxCall(url, data){         
    $.ajax({
        url : url,
        type : 'POST',
        data : JSON.stringify(data),
        contentType : 'application/json utf-8',
        dataType : 'json',
        success : function(data, textStatus, jqxhr){                    
            callBack(data);
        },
        error : function (jqxhr, textStatus, errorMessage){ 
            console.log(argument);              
        }               
    })
}
