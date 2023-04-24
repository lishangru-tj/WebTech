export function getDomDate (str) {
  var yearIndex = str.indexOf("年");
  var startIndex = yearIndex - 4;
  var endIndex = str.indexOf("日") + 1;
  var date = str.substring(startIndex, endIndex);
  return date;
}

export function getDomYear(str){
  var str = getDomDate(str);
  var year = str.substring(0,4);
  return year;
}

export function getDomMonth(str){
  var str = getDomDate(str);
  var nianIndex = str.indexOf("年") + 1;
  var yueIndex = str.indexOf("月");
  var month = str.substring(nianIndex, yueIndex);
  return month;
}

export function getDomDay(str){
  var str = getDomDate(str);
  var yueIndex = str.indexOf("月") + 1;
  var riIndex = str.indexOf("日");
  var day = str.substring(yueIndex, riIndex);
  return day;
}