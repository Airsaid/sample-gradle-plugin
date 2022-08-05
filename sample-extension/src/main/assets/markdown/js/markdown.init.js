
var md_content=document.getElementById("md_content");

var div = document.createElement("div");
div.innerHTML = marked(md_content.value);
var imageArray=div.getElementsByTagName("img");
for(var i=0;i<imageArray.length;i++){
    var img=imageArray[i];
    img.setAttribute("width","98%");
}
var content=document.getElementById('content');
content.appendChild(div);