package com.hassanmashraful.democonstract.app;

/**
 * Created by Optimus Prime on 12/18/2016.
 */

public class AppConfig {
    // Server user login url
    public static String URL_LOGIN = "http://23.235.211.194/~tecnot5/dev/construction/v1/operators/login";


    // Server insert shift url
    public static String URL_INSERT_SHIFT = "http://23.235.211.194/~tecnot5/dev/construction/v1/shifts";

    // Server update shift url
    public static String URL_UPDATE_SHIFT = "http://23.235.211.194/~tecnot5/dev/construction/v1/shifts/";

    // Server user check_out_message url
    public static String URL_INSERT_CHECKOUT_MESSAGE = "http://23.235.211.194/~tecnot5/dev/construction/v1/checkout_messages";

    // Server user operational check url
    public static String URL_INSERT_OPERATIONAL_CHECK = "http://23.235.211.194/~tecnot5/dev/construction/v1/operantional_checks";

    //Server get vehicles by category
    public static String URL_TRUCK = "http://23.235.211.194/~tecnot5/dev/construction/v1/trucks/vehicle/";

    //Server get all vehicles available in database
    public static String URL_ALL_VEHICLES = "http://23.235.211.194/~tecnot5/dev/construction/v1/trucks";

    //Server get all vehicles available in database
    public static String URL_INSERT_FUEL_RECORD = "http://23.235.211.194/~tecnot5/dev/construction/v1/fuel_record";

}
