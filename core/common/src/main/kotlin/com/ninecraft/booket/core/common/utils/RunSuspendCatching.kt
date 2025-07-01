package com.ninecraft.booket.core.common.utils

import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.coroutines.cancellation.CancellationException
import kotlin.contracts.ExperimentalContracts

/**
 * suspend `block`을 실행하고, 성공 시 결과를, 실패 시 예외를 `Result`로 반환
 * `CancellationException`은 예외가 아닌, 코루틴의 취소 신호로 인식하여 그대로 전파
 *
 * @param block 실행할 suspend 람다
 * @return `block`의 실행 결과를 담은 [Result]
 */

@OptIn(ExperimentalContracts::class)
@Suppress("WRONG_INVOCATION_KIND", "TooGenericExceptionCaught")
inline fun <T> runSuspendCatching(block: () -> T): Result<T> {
    // 계약(contract): 컴파일러에게 'block' 람다의 실행 시점과 횟수를 명시적으로 알림
    // 'callsInPlace'와 'EXACTLY_ONCE'는 'block'이 이 함수 내에서 즉시, 그리고 정확히 한 번만 실행됨을 보장
    // 이를 통해 람다 내부의 변수 초기화 상태를 올바르게 추론할 수 있음
    contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }
    return try {
        Result.success(block())
    } catch (rethrown: CancellationException) {
        throw rethrown
    } catch (exception: Throwable) {
        Result.failure(exception)
    }
}
