function init() {
    $("#submit_login").click(function () {
        url = "Users?action=LOGIN&dontRedirect=true&userName="+$("#userName").val()+"&userPass="+$("#userPass").val();
        $.ajax({
          url:url,
          success:function(xml){
              //check that access is not denied 
              if($(xml).find("denied").text() === "true")
                  alert("access denied, try again");
             
              saved_themeId = $.cookie("themeId");
              if (saved_themeId !== undefined) {
                  $.get("Choix?action=JUST_THEME&themeId="+saved_themeId+"");
              }
              
              //and refresh the page
              var url = ""+window.location+"";
              var unsafe = false;
              if (url.indexOf("logout") !== -1)
                  unsafe = true;
              if (url.indexOf("/Users") !== -1)
                  unsafe = true;
              if (url.indexOf("#") !== -1) {
                  url = url.substring(0, url.indexOf("#"));
              }
              if (!unsafe)
                  window.location = url;
              else
                  window.location = "Index";
              
              return false;
          },
          async:false
         });
         
        return false;
    }) ;
}
$(init);