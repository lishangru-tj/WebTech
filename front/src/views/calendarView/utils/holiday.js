import { HolidayUtil } from 'lunar-javascript'
export function getHolidayInfo (byear, bmon, bday) {

  let result = "";

  //获取农历
  const { Solar } = require('lunar-javascript')
  let solar = Solar.fromYmd(byear, bmon, bday);
  let str = solar.getLunar().toFullString();
  let yueIndex = str.indexOf("月");
  result = str.substring(yueIndex + 1, yueIndex + 3)

  //获取节假日
  var holiday = HolidayUtil.getHoliday(byear, bmon, bday);

  try {
    var holidayName = holiday.getName();
    if (holidayName != null)
      result = holidayName;
  }
  catch {
    
  }

  return result;
}