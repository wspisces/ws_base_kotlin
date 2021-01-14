package com.ws.support.utils

import android.content.Context
import android.content.SharedPreferences
import com.ws.support.base.BaseApplication

/**
 * 数据缓存类
 */
object SharePreferUtil {
    const val ACCESSTOKEN = "accessToken"
    const val MAX_TEMP = "max_temp"
    const val MIN_TEMP = "min_temp"
    const val TEMP_ID = "temp_id"
    const val AlertTime = "alert_time"
    const val FirstLogin = "firstLogin"
    const val LoginType = "loginType"
    const val ThirdPartyId = "thirdPartyId"
    const val OpenId = "openId"
    const val PersonId = "personId"
    const val DeviceId = "DeviceId"
    const val MacAddrss = "MacAddrss"
    const val AlertTimeBefore = "AlertTimeBefore"
    private const val AUTO_LOGIN = "AutoLogin"
    private const val REMEMBER_PASSWORD = "RememberPassword"
    private const val USER_ACCOUNT = "UserAccount"
    private const val USER_PASSWORD = "UserPassword"
    private const val USER_NAME = "UserName"
    private const val USER_ID = "userId"
    private const val KEY_AUTO_LOGIN = "auto_login"
    private const val KEY_REMEMBER_PWD = "remember_password"
    private const val PHONE = "phone"
    private const val ISVERIFY = "isVerify"
    private var TAG = "andu_tempratrue"
    fun setTag(code: String) {
        TAG = code
    }

    fun getEditor(): SharedPreferences.Editor {
        val sharedPreferences: SharedPreferences = BaseApplication.Companion.getInstance()!!.getSharedPreferences(TAG, Context.MODE_PRIVATE)
        return sharedPreferences.edit()
    }

    fun getSharedPreferences(): SharedPreferences {
        return BaseApplication.Companion.getInstance()!!.getSharedPreferences(TAG, Context.MODE_PRIVATE)
    }

    fun getString(str: String?): String? {
        return getSharedPreferences().getString(str, "")
    }

    fun isAutoLogin(): Boolean {
        return getSharedPreferences().getBoolean(AUTO_LOGIN, false)
    }

    fun saveAutoLogin(isAuto: Boolean): Boolean {
        return getEditor().putBoolean(AUTO_LOGIN, isAuto).commit()
    }

    fun isRemeberPassward(): Boolean {
        return getSharedPreferences().getBoolean(REMEMBER_PASSWORD, true)
    }

    fun saveRemeberPassward(isRemember: Boolean): Boolean {
        return getEditor().putBoolean(REMEMBER_PASSWORD, isRemember).commit()
    }

    fun getUserAccount(): String? {
        return getSharedPreferences().getString(USER_ACCOUNT, "")
    }

    fun saveUserAccount(account: String?): Boolean {
        return getEditor().putString(USER_ACCOUNT, account).commit()
    }

    fun getUserPassword(): String? {
        return getSharedPreferences().getString(USER_PASSWORD, "")
    }

    fun saveUserPassword(pwd: String?): Boolean {
        return getEditor().putString(USER_PASSWORD, pwd).commit()
    }

    fun getUserName(): String? {
        return getSharedPreferences().getString(USER_NAME, "")
    }

    fun saveUserName(name: String?): Boolean {
        return getEditor().putString(USER_NAME, name).commit()
    }

    fun getUserId(): String? {
        return getSharedPreferences().getString(USER_ID, "")
    }

    fun saveUserId(userId: String?): Boolean {
        return getEditor().putString(USER_ID, userId).commit()
    }

    //    /*---记住密码---*/
    //    public static void saveRemPwd( boolean flag) {
    //        getDefaultEditor().putBoolean(KEY_REMEMBER_PWD, flag).commit();
    //    }
    //
    //    public static boolean getRemPwd() {
    //        return getDefaultSharedPreferences().getBoolean(KEY_REMEMBER_PWD, true);
    //    }
    //
    /*---Phone---*/
    fun savePhone(phone: String?) {
        getEditor().putString(PHONE, phone).commit()
    }

    fun getPhone(): String? {
        return getSharedPreferences().getString(PHONE, "")
    }

    fun saveAccessToken(accessToken: String?) {
        getEditor().putString(ACCESSTOKEN, accessToken).commit()
    }

    fun getAccessToken(): String? {
        return getSharedPreferences().getString(ACCESSTOKEN, "")
    }

    fun saveMaxTemperature(max: Float) {
        getEditor().putFloat(MAX_TEMP, max).commit()
    }

    fun getMaxTemperature(): Float {
        return getSharedPreferences().getFloat(MAX_TEMP, 38.5f)
    }

    fun saveMinTemperature(max: Float) {
        getEditor().putFloat(MIN_TEMP, max).commit()
    }

    fun getMinTemperature(): Float {
        return getSharedPreferences().getFloat(MIN_TEMP, 35.5f)
    }

    fun saveTemperatureId(id: Int) {
        getEditor().putInt(TEMP_ID, id).commit()
    }

    fun getTemperatureId(): Int {
        return getSharedPreferences().getInt(TEMP_ID, -1)
    }

    fun saveAlertTime(time: Int) {
        getEditor().putInt(AlertTime, time).commit()
    }

    fun getAlertTime(): Int {
        return getSharedPreferences().getInt(AlertTime, 30)
    }

    fun saveFirstLogin() {
        getEditor().putBoolean(FirstLogin, false).commit()
    }

    fun getFirstLogin(): Boolean {
        return getSharedPreferences().getBoolean(FirstLogin, true)
    }

    fun saveLoginType(loginType: String?) {
        getEditor().putString(LoginType, loginType).commit()
    }

    fun getLoginType(): String? {
        return getSharedPreferences().getString(LoginType, "")
    }

    fun saveThirdPartyId(thirdPartyId: String?) {
        getEditor().putString(ThirdPartyId, thirdPartyId).commit()
    }

    fun getThirdPartyId(): String? {
        return getSharedPreferences().getString(ThirdPartyId, "")
    }

    fun saveOpenId(openId: String?) {
        getEditor().putString(OpenId, openId).commit()
    }

    fun getOpenId(): String? {
        return getSharedPreferences().getString(OpenId, "")
    }

    fun saveCurrentPersonId(personId: Int) {
        getEditor().putInt(PersonId, personId).commit()
    }

    fun getCurrentPersonId(): Int {
        return getSharedPreferences().getInt(PersonId, -1)
    }

    fun saveCurrentDeviceId(deviceId: String?) {
        getEditor().putString(DeviceId, deviceId).commit()
    }

    fun getCurrentDeviceId(): String? {
        return getSharedPreferences().getString(DeviceId, "")
    }

    fun saveCurrentDeviceMac(macAddrss: String?) {
        getEditor().putString(MacAddrss, macAddrss).commit()
    }

    fun getCurrentDeviceMac(): String? {
        return getSharedPreferences().getString(MacAddrss, "")
    }

    fun saveAlertTimeBefore(time: Long) {
        getEditor().putLong(AlertTimeBefore, time).commit()
    }

    fun getAlertTimeBefore(): Long {
        return getSharedPreferences().getLong(AlertTimeBefore, 0)
    }

    fun saveIsVerify(isVerify: Boolean) {
        getEditor().putBoolean(ISVERIFY, isVerify).commit()
    }

    fun isVerify(): Boolean {
        return getSharedPreferences().getBoolean(ISVERIFY, false)
    }

    const val IS_PORTRAIT = "isPortrait"
    fun saveScreenOrientation(isPortrait: Boolean) {
        getEditor().putBoolean(IS_PORTRAIT, isPortrait).commit()
    }

    fun getScreenOrientation(): Boolean {
        return getSharedPreferences().getBoolean(IS_PORTRAIT, false)
    }

    //清理缓存数据
    fun ClearCache() {
        savePhone("")
        saveAccessToken("")
        saveTemperatureId(-1)
        saveLoginType("")
        saveThirdPartyId("")
        saveOpenId("")
        saveCurrentPersonId(-1)
        saveCurrentDeviceId("")
        saveCurrentDeviceMac("")
        saveAlertTimeBefore(0)
    }
}