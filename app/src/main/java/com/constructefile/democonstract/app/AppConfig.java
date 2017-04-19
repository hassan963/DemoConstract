package com.constructefile.democonstract.app;

/**
 * Created by Optimus Prime on 12/18/2016.
 */

public class AppConfig {

    // Server user login url
    public static String URL_LOGIN = "http://23.235.211.194/~tecnot5/dev/construction/v1/operators/login";

    // Server insert shift url
    public static String URL_INSERT_SHIFT = "http://constructefile.com/v1/shifts";

    // Server update shift url
    public static String URL_UPDATE_SHIFT = "http://constructefile.com/v1/shifts/";

    // Server user check_out_message url
    public static String URL_INSERT_CHECKOUT_MESSAGE = "http://constructefile.com/v1/checkout_messages";

    // Server user operational check url
    public static String URL_INSERT_OPERATIONAL_CHECK = "http://constructefile.com/v1/operantional_checks";

    //Server get vehicles by category
    public static String URL_TRUCK = "http://constructefile.com/v1/trucks/vehicle/";

    //Server get all vehicles available in database
    public static String URL_ALL_VEHICLES = "http://23.235.211.194/~tecnot5/dev/construction/v1/trucks";

    //Server get all vehicles available in database
    public static String URL_INSERT_FUEL_RECORD = "http://23.235.211.194/~tecnot5/dev/construction/v1/equipment_fuel";

    //Server INJURY REPORT POST
    public static String URL_INSERT_INJURY_REPORT = "http://23.235.211.194/~tecnot5/dev/construction/v1/incident_report";

    //Server  NEAR MISS POST
    public static String URL_INSERT_NEAR_MISS = "http://23.235.211.194/~tecnot5/dev/construction/v1/nearmiss";

    //Server  UNSAFE WORK NOTIFY
    public static String URL_INSERT_UNSAFE_WORK = "http://23.235.211.194/~tecnot5/dev/construction/v1/unsafe_work_notification";

    //Server  UNSAFE WORK NOTIFY
    public static String URL_GET_SUPERVISOR = "http://23.235.211.194/~tecnot5/dev/construction/v1/supervisors";


    //Server get all time sheet available in database
    public static String URL_ALL_EQUIPMENTS = "http://23.235.211.194/~tecnot5/dev/construction/v1/equipment";

    //Server insert time sheet in database
    public static String URL_INSERT_TIMESHEET = "http://23.235.211.194/~tecnot5/dev/construction/v1/timesheet";

    public static String GET_ALL_DAILY_TASK = "http://23.235.211.194/~tecnot5/dev/construction/v1/daily_task/today/operator/";

    public static String GET_TODAY_TAILGATE_MEETING = "http://23.235.211.194/~tecnot5/dev/construction/v1/tailgate_meeting/today/operator/";

    public static String URL_UPDATE_TODAY_TAILGATE_MEETING = "http://23.235.211.194/~tecnot5/dev/construction/v1/tailgate_meeting/";

    public static String GET_TODAY_HAZARD_ASSESMENT = "http://23.235.211.194/~tecnot5/dev/construction/v1/hazard_assessment/today/operator/";

    public static String URL_UPDATE_TODAY_HAZARD_ASSESMENT = "http://23.235.211.194/~tecnot5/dev/construction/v1/hazard_assessment/";

    public static String GET_SUPERVISOR_NAME_BY_ID = "http://23.235.211.194/~tecnot5/dev/construction/v1/supervisors/";

}
