package com.feidroid.sms.autoreply.util;

public interface TelephonyPropertiesUtil {


/**
 * Contains a list of string constants used to get or set telephone properties
 * in the system. You can use {@link android.os.SystemProperties os.SystemProperties}
 * to get and set these values.
 * @hide
 */
    //****** Baseband and Radio Interface version

    //TODO T: property strings do not have to be gsm specific
    //        change gsm.*operator.*" properties to "operator.*" properties

    /**
     * Baseband version
     * Availability: property is available any time radio is on
     */
    static final String PROPERTY_BASEBAND_VERSION = "gsm.version.baseband";

    static final String PROPERTY_CDMA_BASEBAND_VERSION = "gsm.version.baseband.cdma";

    /** Radio Interface Layer (RIL) library implementation. */
    static final String PROPERTY_RIL_IMPL = "gsm.version.ril-impl";

    //****** Current Network

    /** Alpha name of current registered operator.<p>
     *  Availability: when registered to a network. Result may be unreliable on
     *  CDMA networks.
     */
    static final String PROPERTY_OPERATOR_ALPHA = "gsm.operator.alpha";
    //TODO: most of these proprieties are generic, substitute gsm. with phone. bug 1856959

    /** Numeric name (MCC+MNC) of current registered operator.<p>
     *  Availability: when registered to a network. Result may be unreliable on
     *  CDMA networks.
     */
    static final String PROPERTY_OPERATOR_NUMERIC = "gsm.operator.numeric";

    /** 'true' if the device is on a manually selected network
     *
     *  Availability: when registered to a network
     */
    static final String PROPERTY_OPERATOR_ISMANUAL = "operator.ismanual";

    /** 'true' if the device is considered roaming on this network for GSM
     *  purposes.
     *  Availability: when registered to a network
     */
    static final String PROPERTY_OPERATOR_ISROAMING = "gsm.operator.isroaming";

    // P1_KOR_Add [[
    //kiseong.jang_100502 add 3gr signal [[
    /** 'true' if the device is considered 3G roaming on this network for GSM
     *  purposes.
     *  Availability: when registered to a network
     */    
    static final String PROPERTY_OPERATOR_IS3GROAMING = "gsm.operator.is3groaming";
    //kiseong.jang_100502 add 3gr signal ]]
    // ]]

    /** The ISO country code equivalent of the current registered operator's
     *  MCC (Mobile Country Code)<p>
     *  Availability: when registered to a network. Result may be unreliable on
     *  CDMA networks.
     */
    static final String PROPERTY_OPERATOR_ISO_COUNTRY = "gsm.operator.iso-country";

    static final String CURRENT_ACTIVE_PHONE = "gsm.current.phone-type";

    ///////////////CONFIG_CTC_CARD_FRAMEWORK start
    // [NAM] 2010.07.05 - Separate value of Property for CDMA and GSM.
    static final String PROPERTY_OPERATOR_ALPHA_CDMA = "gsm.operator.alpha.cdma"; 
    static final String PROPERTY_OPERATOR_NUMERIC_CDMA = "gsm.operator.numeric.cdma";
    static final String PROPERTY_OPERATOR_ISROAMING_CDMA = "gsm.operator.isroaming.cdma";
    static String PROPERTY_DATA_NETWORK_TYPE_CDMA = "gsm.network.type.cdma";    
    /////////////  

    ///////////// 
    // [NAM] 2010.07.14 property for Airplain mode check
    static final String PROPERTY_AIRPLAIN_CDMA = "gsm.airplain.cdma"; 
    static final String PROPERTY_AIRPLAIN_GSM = "gsm.airplain.gsm"; 
    ///////////// //CONFIG_CTC_CARD_FRAMEWORK end

    //****** SIM Card
    /**
     * One of <code>"UNKNOWN"</code> <code>"ABSENT"</code> <code>"PIN_REQUIRED"</code>
     * <code>"PUK_REQUIRED"</code> <code>"NETWORK_LOCKED"</code> or <code>"READY"</code>
     */
    static String PROPERTY_SIM_STATE = "gsm.sim.state";
    static String PROPERTY_RUIM_STATE = "gsm.ruim.state"; //CONFIG_CTC_CARD_FRAMEWORK 

	static String PROPERTY_SIM_NEWCHECK = "gsm.sim.newCheck"; //CONFIG_CTC_CARD_FRAMEWORK 
    static String PROPERTY_RUIM_NEWCHECK = "gsm.ruim.newCheck";//CONFIG_CTC_CARD_FRAMEWORK 
    /** The MCC+MNC (mobile country code+mobile network code) of the
     *  provider of the SIM. 5 or 6 decimal digits.
     *  Availablity: SIM state must be "READY"
     */
    static String PROPERTY_ICC_OPERATOR_NUMERIC = "gsm.sim.operator.numeric";
    static String PROPERTY_RUIM_OPERATOR_NUMERIC = "gsm.ruim.operator.numeric";//CONFIG_CTC_CARD_FRAMEWORK 

    /** PROPERTY_ICC_OPERATOR_ALPHA is also known as the SPN, or Service Provider Name.
     *  Availablity: SIM state must be "READY"
     */
    static String PROPERTY_ICC_OPERATOR_ALPHA = "gsm.sim.operator.alpha";
    static String PROPERTY_RUIM_OPERATOR_ALPHA = "gsm.ruim.operator.alpha";//CONFIG_CTC_CARD_FRAMEWORK 

    /** ISO country code equivalent for the SIM provider's country code*/
    static String PROPERTY_ICC_OPERATOR_ISO_COUNTRY = "gsm.sim.operator.iso-country";
    static String PROPERTY_RUIM_OPERATOR_ISO_COUNTRY = "gsm.ruim.operator.iso-country";//CONFIG_CTC_CARD_FRAMEWORK 

    /**
     * MSISDN
     */
    static final String PROPERTY_SIM_MSISDN = "gsm.sim.msisdn";
    static final String PROPERTY_RUIM_MSISDN = "gsm.ruim.msisdn";//CONFIG_CTC_CARD_FRAMEWORK 

    /**
     * Indicates the available radio technology.  Values include: <code>"unknown"</code>,
     * <code>"GPRS"</code>, <code>"EDGE"</code> and <code>"UMTS"</code>.
     */
    static String PROPERTY_DATA_NETWORK_TYPE = "gsm.network.type";

    /** Indicate if phone is in emergency callback mode */
    static final String PROPERTY_INECM_MODE = "ril.cdma.inecmmode";

    /** Indicate the timer value for exiting emergency callback mode */
    static final String PROPERTY_ECM_EXIT_TIMER = "ro.cdma.ecmexittimer";

    /** The international dialing prefix conversion string */
    static final String PROPERTY_IDP_STRING = "ro.cdma.idpstring";

    /**
     * Defines the schema for the carrier specified OTASP number
     */
    static final String PROPERTY_OTASP_NUM_SCHEMA = "ro.cdma.otaspnumschema";

    /**
     * Disable all calls including Emergency call when it set to true.
     */
    static final String PROPERTY_DISABLE_CALL = "ro.telephony.disable-call";

    /**
     * Set to true for vendor RIL's that send multiple UNSOL_CALL_RING notifications.
     */
    static final String PROPERTY_RIL_SENDS_MULTIPLE_CALL_RING =
        "ro.telephony.call_ring.multiple";

    /**
     * The number of milli-seconds between CALL_RING notifications.
     */
    static final String PROPERTY_CALL_RING_DELAY = "ro.telephony.call_ring.delay";

    /**
     * Track CDMA SMS message id numbers to ensure they increment
     * monotonically, regardless of reboots.
     */
    static final String PROPERTY_CDMA_MSG_ID = "persist.radio.cdma.msgid";

    /**
     * Property to override DEFAULT_WAKE_LOCK_TIMEOUT
     */
    static final String PROPERTY_WAKE_LOCK_TIMEOUT = "ro.ril.wake_lock_timeout";

    /**
     * Set to true to indicate that the modem needs to be reset
     * when there is a radio technology change.
     */
    static final String PROPERTY_RESET_ON_RADIO_TECH_CHANGE = "persist.radio.reset_on_switch";
	
	/**
     * SMS GCF Mode
     */
    static final String PROPERTY_SMS_GCF_MODE = "ril.sms.gcf-mode";
    static final String PROPERTY_DEFAULT_ESN = "gsm.default.esn"; //CONFIG_CTC_CARD_FRAMEWORK 
    static final String PROPERTY_CHANNEL_WHEN_DEFAULT_ESN = "gsm.default.channel";//CONFIG_CTC_CARD_FRAMEWORK 
    static final String PROPERTY_SIO_MODE_WHEN_DEFAULT_ESN = "gsm.default.siomode";//CONFIG_CTC_CARD_FRAMEWORK 

    static String PROPERTY_CTC_GSM_DATA_REQUIREMENT = "ctc.gsm.data.requirement";	

	/* //Start CONFIG_CTC_CARD_FRAMEWORK 
	* NEW CTC SIM CARD Concept
	*/
	static String PROPERTY_GSM_AVAILBILITY = "gsm.gsm.availability";
	static String PROPERTY_CDMA_AVAILBILITY = "gsm.ruim.availability";

	static String PROPERTY_GSM_PPLOCK = "gsm.gsm.pplock";
	static String PROPERTY_CDMA_PPLOCK = "gsm.ruim.pplock";

	static String PROPERTY_GSM01_CARDNAME = "gsm.gsm.cardname";
	static String PROPERTY_GSM02_CARDNAME = "gsm.gsmdual.cardname";
	static String PROPERTY_CDMA_CARDNAME = "gsm.ruim.cardname";

	static String PROPERTY_GSM01_ICON = "gsm.gsm.icon";
	static String PROPERTY_GSM02_ICON = "gsm.gsmdual.icon";
	static String PROPERTY_CDMA_ICON = "gsm.ruim.icon";
	
	static String PROPERTY_CDMA_ACTIVITY = "gsm.ruim.activity";
	static String PROPERTY_GSM01_ACTIVITY = "gsm.gsm.activity";
	static String PROPERTY_GSM02_ACTIVITY = "gsm.gsmdual.activity";
	
	static String PROPERTY_CDMA_STATUS = "gsm.ruim.currentcardstatus";
	static String PROPERTY_GSM_STATUS = "gsm.gsm.currentcardstatus";


   //2012.04.25  YS
	static String PROPERTY_CDMA_RESET = "gsm.ruim.rilcardstatus";
	static String PROPERTY_GSM_RESET = "gsm.gsm.rilcardstatus";
	 //End CONFIG_CTC_CARD_FRAMEWORK 
}