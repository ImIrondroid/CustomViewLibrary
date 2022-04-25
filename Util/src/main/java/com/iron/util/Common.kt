package com.iron.util

/**
 * @author 최철훈
 * @created 2022-04-25
 * @desc 모듈내에서 자주 쓰이는 함수 정의
 */

inline fun <T: Any> ifLet(vararg elements: T?, closure: (List<T>) -> Unit) {
    if (elements.all { it != null }) {
        closure(elements.filterNotNull())
    }
}