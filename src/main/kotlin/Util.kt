fun keys(obj: dynamic): Array<String> {
    return js("Object").keys(obj).unsafeCast<Array<String>>()
}

/**
 * Recursively get all keys.  Doesn't duplicate paths, that is if
 * the object is:
 * {
 *     a: {
 *       b: ...
 *       c: ...
 *     }
 *     d: ...
 * }
 * It will return:
 * a.b, a.c, d
 */
fun getAllKeys(obj: dynamic): List<String> {
    if (obj == undefined) {
        return listOf()
    }
    return keys(obj).flatMap { key ->
        if (jsTypeOf(obj[key]) == "object") {
            (getAllKeys(obj[key]).map { "$key.$it" })
        } else {
            listOf(key)
        }
    }
}

/**
 * Get the value of a dot-separated path
 */
// TODO: better way to do this?
fun getValue(obj: dynamic, path: String): dynamic {
    if (path.isBlank()) {
        return obj
    }
    val paths = path.split(".")
    return getValue(obj[paths.first()], paths.drop(1).joinToString("."))
}

@Suppress("UnsafeCastFromDynamic")
fun isNumber(obj: dynamic): Boolean {
    return jsTypeOf(obj) === "number"
}

inline fun jsObject(init: dynamic.() -> Unit): dynamic {
    val o = js("{}")
    init(o)
    return o
}
