package com.example.enjoysdk;

public class EnjoyErrorCode {
    /*************************************************************************************************/
    /*          错误码统一类                                                                          */
    /*          格式： ENJOY_${Manager}_ERROR_${Error Type} 所有字母大写                               */
    /*          1、所有错误码必须为负数                                                                */
    /*          2、所有错误码不能相同                                                                  */
    /*          3、所有错误码必须在指定的区间内进行分配                                                 */
    /*************************************************************************************************/



    /*************************************************************************************************/
    /*          0:successful, -1 ~ -100 Enjoy COMMON ERROR CODE                                      */
    /*************************************************************************************************/
    public final static int ENJOY_COMMON_SUCCESSFUL = 0;
    public final static int ENJOY_COMMON_ERROR_UNKNOWN = -1;
    public final static int ENJOY_COMMON_ERROR_DEVICE_NOT_SUPPORT = -2;
    public final static int ENJOY_COMMON_ERROR_SDK_NOT_SUPPORT = -3;
    public final static int ENJOY_COMMON_ERROR_WRITE_SETTINGS_ERROR = -4;
    public final static int ENJOY_COMMON_ERROR_SERVICE_NOT_START = -5;

    /*************************************************************************************************/
    /*                        -101 ~ -200 Enjoy EthernetManager ERROR CODE                           */
    /*************************************************************************************************/
    public final static int ENJOY_ETHERNET_MANAGER_ERROR_CONFIG_INVALID = -101;
    public final static int ENJOY_ETHERNET_MANAGER_ERROR_ETH_TETHER_IN_USE = -102;

    /*************************************************************************************************/
    /*                        -201 ~ -300 Enjoy PowerManager ERROR CODE                              */
    /*************************************************************************************************/
    public final static int ENJOY_POWER_MANAGER_TIMED_TOUCH_WAKE_INFO_ERROR = -201;
    public final static int ENJOY_POWER_MANAGER_TIMED_TOUCH_WAKE_INFO_CONFLICT = -202;
    public final static int ENJOY_POWER_MANAGER_TIMED_TOUCH_WAKE_PLAN_NOT_EXITS = -203;
    public final static int ENJOY_POWER_MANAGER_TIMED_TOUCH_WAKE_PLAN_NOT_START = -204;
    public final static int ENJOY_POWER_MANAGER_MANUAL_CONFIG_BATTERY_NOT_ALLOW = -205;
    public final static int ENJOY_POWER_MANAGER_BATTERY_LEVEL_INFO_ERROR = -206;
    public final static int ENJOY_POWER_MANAGER_BATTERY_STATE_INFO_ERROR = -207;
    public final static int ENJOY_POWER_MANAGER_BATTERY_THRESHOLDVALUE_INFO_ERROR = -208;

    /*************************************************************************************************/
    /*                          -301 ~ -400 Enjoy SecureManager ERROR CODE                           */
    /*************************************************************************************************/
    public final static int ENJOY_SECURE_MANAGER_ERROR_PASSWD_CHECK_FAILED = -301;
    public final static int ENJOY_SECURE_MANAGER_ERROR_PASSWD_FORMAT_ERROR = -302;
    public final static int ENJOY_SECURE_MANAGER_ERROR_PASSWD_ALREADY_EMPYT = -303;
    public final static int ENJOY_SECURE_MANAGER_ERROR_PASSWD_SET_FAILED = -304;
    public final static int ENJOY_SECURE_MANAGER_ERROR_PASSWD_NOT_INIT = -305;
    public final static int ENJOY_SECURE_MANAGER_ERROR_REGISTER_SAFE_PROGRAM_FAILED = -306;
    public final static int ENJOY_SECURE_MANAGER_ERROR_UNREGISTER_SAFE_PROGRAM_FAILED = -307;
    public final static int ENJOY_SECURE_MANAGER_ERROR_PROGRAM_NOT_IN_SAFE_PROGRAM_LIST = -308;
    public final static int ENJOY_SECURE_MANAGER_ERROR_PROGRAM_ALREADY_IN_SAFE_PROGRAM_LIST = -309;

    /*************************************************************************************************/
    /*                               -401 ~ -500 Enjoy SystemUI ERROR CODE                           */
    /*************************************************************************************************/

    /*************************************************************************************************/
    /*                               -501 ~ -600 Enjoy TimeManager ERROR CODE                        */
    /*************************************************************************************************/
    public final static int ENJOY_TIME_MANAGER_ERROR_PARAMETER_ERROR = -501;
    public final static int ENJOY_TIME_MANAGER_ERROR_FUNCTION_OCCUPY = -502;
    public final static int ENJOY_TIME_MANAGER_ERROR_NTP_SERVER_ADDRESS_SET_FAILED = -503;
    public final static int ENJOY_TIME_MANAGER_ERROR_NTP_TIMEOUT_SET_FAILED = -504;
    public final static int ENJOY_TIME_MANAGER_ERROR_NTP_CONFIG_SET_FAILED = -505;


    /*************************************************************************************************/
    /*                               -601 ~ -700 Enjoy InstallManager ERROR CODE                     */
    /*************************************************************************************************/
    public static final int ENJOY_INSTALL_MANAGER_ERROR_WHITE_ADD = -601;
    public static final int ENJOY_INSTALL_MANAGER_ERROR_WHITE_REMOVE = -602;

    /*************************************************************************************************/
    /*                               -701 ~ -800 Enjoy RotationManager ERROR CODE                    */
    /*************************************************************************************************/
    public static final int ENJOY_ROTATION_MANAGER_ERROR_DISPLAY_ROTATION = -701;
    public static final int ENJOY_ROTATION_MANAGER_ERROR_SYSTEM_ROTATION = -702;
    public static final int ENJOY_ROTATION_MANAGER_ERROR_VICE_ROTATION = -703;
    public static final int ENJOY_ROTATION_MANAGER_ERROR_ROTATION_FORMAT = -704;
    public static final int ENJOY_ROTATION_MANAGER_ERROR_VICE_FULL = -705;

    /*************************************************************************************************/
    /*                               -801 ~ -900 Enjoy BootanimationManager ERROR CODE               */
    /*************************************************************************************************/
    public final static int ENJOY_BOOTANIMATION_MANAGER_ERROR_FILE_NOT_EXIST = -801;
    public final static int ENJOY_BOOTANIMATION_MANAGER_ERROR_FILE_CHECK_FAILED = -802;
    public final static int ENJOY_BOOTANIMATION_MANAGER_ERROR_FILE_COPY_FAILED = -803;
    public final static int ENJOY_BOOTANIMATION_MANAGER_ERROR_PARAMETER_WRONG = -804;
    public final static int ENJOY_BOOTANIMATION_MANAGER_ERROR_CAN_NOT_DELETE_FILE = -805;
    public final static int ENJOY_BOOTANIMATION_MANAGER_ERROR_FILE_TYPE_WRONG = -806;
    public final static int ENJOY_BOOTANIMATION_MANAGER_ERROR_NOT_ZIP_FILE = -807;

    /*************************************************************************************************/
    /*                               -901 ~ -1000 Enjoy HomeManager ERROR CODE                       */
    /*************************************************************************************************/
    public final static int ENJOY_HOME_MANAGER_ERROR_PACKAGE_NOT_EXIST = -901;

    /*************************************************************************************************/
    /*                               -1001 ~ -1100 Enjoy HardwareStatusManagerService ERROR CODE     */
    /*************************************************************************************************/

    /*************************************************************************************************/
    /*                               -1101 ~ -1200 Enjoy HardwareKeyBoardService ERROR CODE          */
    /*************************************************************************************************/


    /*************************************************************************************************/
    /*                               -1201 ~ -1300 Enjoy WatchDogManager ERROR CODE                  */
    /*************************************************************************************************/
    public static final int ENJOY_WATCHDOG_MANAGER_ERROR_NOT_INIT = -1201;
    public static final int ENJOY_WATCHDOG_MANAGER_ERROR_INIT_AGAIN = -1202;
    public static final int ENJOY_WATCHDOG_MANAGER_ERROR_INVALID_TIMEOUT = -1203;
    public static final int ENJOY_WATCHDOG_MANAGER_ERROR_OPEN_DEV = -1204;
    public static final int ENJOY_WATCHDOG_MANAGER_ERROR_SET_TIMEOUT_ERROR = -1205;

    /*************************************************************************************************/
    /*                               -1301 ~ -1400 Enjoy EthTetherManager ERROR CODE                 */
    /*************************************************************************************************/
    public static final int ENJOY_ETHTETHER_MANAGER_ERROR_INVALID_UPSTREAM    = -1301;
    public static final int ENJOY_ETHTETHER_MANAGER_ERROR_INVALID_CONFIG    = -1302;

    /*************************************************************************************************/
    /*                               -1401 ~ -1500 Enjoy NetCoexistence ERROR CODE                   */
    /*************************************************************************************************/

    /*************************************************************************************************/
    /*                               -1501 ~ -1600 Enjoy WatchDogManager ERROR CODE                  */
    /*************************************************************************************************/
    public static final int ENJOY_SOUND_MANAGER_ERROR_NOT_INIT = -1501;
    public static final int ENJOY_SOUND_MANAGER_ERROR_INIT_AGAIN = -1502;
    public static final int ENJOY_SOUND_MANAGER_ERROR_INVALID_VALUE = -1503;
    public static final int ENJOY_SOUND_MANAGER_ERROR_OPEN_DEV = -1504;
    public static final int ENJOY_SOUND_MANAGER_ERROR_SET_VALUE_ERROR = -1505;

    /*************************************************************************************************/
    /*                               -1601 ~ -1700 Enjoy KeepAliveAPPManager ERROR CODE              */
    /*************************************************************************************************/
    public static final int ENJOY_KEEP_ALIVE_MANAGER_TIME_INFO_ERROR = -1601;
    public static final int ENJOY_KEEP_ALIVE_MANAGER_IS_KEEP_ALIVE = -1602;
    public static final int ENJOY_KEEP_ALIVE_MANAGER_NOT_KEEP_ALIVE = -1603;
    public static final int ENJOY_KEEP_ALIVE_MANAGER_ERROR_WHITE_ADD = -1604;
    public static final int ENJOY_KEEP_ALIVE_MANAGER_ERROR_WHITE_REMOVE = -1605;
    public static final int ENJOY_KEEP_ALIVE_MANAGER_UPDATE_FAILED = -1606;

    /*************************************************************************************************/
    /*                               -2001 ~ -2100 Enjoy McFirmwareInfoManager ERROR CODE              */
    /*************************************************************************************************/

    public static final int ENJOY_FIRMWARE_INFO_TOUCH_COUNT_CONFIG_ERROR = -2001;
    public static final int ENJOY_FIRMWARE_INFO_TOUCH_COUNT_NOT_CONFIG = -2002;
}