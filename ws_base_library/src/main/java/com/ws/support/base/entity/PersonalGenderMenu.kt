package com.ws.support.base.entity

/**
 * 人员性别枚举
 *
 * @author Johnny.xu
 * date 2017/5/18
 */
enum class PersonalGenderMenu(var type: Int, var gender: String, var isMan: Boolean) {
    Man(0, "男", true), Women(1, "女", false);

    companion object {
        fun getGender(type: Int): String {
            for (menu in values()) {
                if (menu.type == type) {
                    return menu.gender
                }
            }
            return Man.gender
        }

        fun isMan(type: Int): Boolean {
            for (menu in values()) {
                if (menu.type == type) {
                    return menu.isMan
                }
            }
            return Man.isMan
        }

        fun getType(gender: String): Int {
            for (menu in values()) {
                if (menu.gender == gender) {
                    return menu.type
                }
            }
            return Man.type
        }
    }
}