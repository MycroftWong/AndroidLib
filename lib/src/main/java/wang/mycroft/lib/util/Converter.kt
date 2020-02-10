package wang.mycroft.lib.util

/**
 * 将数值转化为[Int]
 *
 * @param value 应该被转化为[Int]类型的值
 * @param defaultValue 若转化失败的默认值
 * @return 转化后的[Int]类型值，若转化失败，返回[defaultValue]
 */
fun convert2Int(value: Any?, defaultValue: Int): Int {
    return if (value == null || "" == value.toString().trim { it <= ' ' }) {
        defaultValue
    } else {
        val valueStr = value.toString()
        valueStr.toIntOrNull() ?: valueStr.toDoubleOrNull()?.toInt() ?: defaultValue
    }
}

/**
 * 将数值转化为[Long]类型
 *
 * @param value 应该被转化为[Long]类型的值
 * @param defaultValue 若转化失败的默认值
 * @return 转化后的[Long]类型值，若转化失败，返回[defaultValue]
 */
fun convert2Long(value: Any?, defaultValue: Long): Long {
    return if (value == null || "" == value.toString().trim { it <= ' ' }) {
        defaultValue
    } else {
        val valueStr = value.toString()
        valueStr.toLongOrNull() ?: valueStr.toDoubleOrNull()?.toLong() ?: defaultValue
    }
}