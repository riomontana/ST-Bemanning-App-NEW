package com.stbemanning.api;

/**
 * Class describing constants for API calls
 * @author Alex Giang, Sanna Roengaard, Simon Borjesson,
 * Lukas Persson, Nikola Pajovic, Linus Forsberg
 */

public class Api {

    private static final String ROOT_URL = "https://stbemanning.com/api/v1/Api.php?apicall=";

    public static final String URL_GET_USER = ROOT_URL + "getUser";
    public static final String URL_GET_WORK_SHIFTS = ROOT_URL + "getWorkShifts";
    public static final String URL_GET_SPECIAL_WORK_SHIFTS = ROOT_URL + "getSpecialWorkShifts";
    public static final String URL_UPDATE_WORK_SHIFT = ROOT_URL + "updateWorkShift";
    public static final String URL_UPDATE_APP_TOKEN = ROOT_URL + "updateAppToken";
    public static final String URL_ADD_APP_TOKEN = ROOT_URL + "addAppToken";
    public static final String URL_GET_APP_TOKEN = ROOT_URL + "getAppToken";


}
