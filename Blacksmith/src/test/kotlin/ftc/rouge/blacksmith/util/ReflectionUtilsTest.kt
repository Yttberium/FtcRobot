package ftc.rouge.blacksmith.util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

// ((Mostly) generated by ChatGPT)
class ReflectionUtilsTest {
    @Nested
    inner class GetMethodTests {
        @Test
        fun `ensure getMethod returns right method that corresponds to arg classes 1-1`() {
            val obj = TestClass()
            val method = obj.getMethod("testMethod", TestParam1::class.java, TestParam1::class.java)

            assertEquals("testMethod", method.name)
            assertTrue(method.parameterTypes.contains(TestParam1::class.java))
        }

        @Test
        fun `ensure getMethod returns right method that corresponds to arg classes 1-2`() {
            val obj = TestClass()
            val method = obj.getMethod("testMethod", TestParam1::class.java, TestParam2::class.java)

            assertEquals("testMethod", method.name)
            assertTrue(method.parameterTypes.contains(TestParam2::class.java))
        }
    }

    @Nested
    inner class InvokeMethodTests {
        @Test
        fun `ensure invokeMethod works lol 1-1`() {
            val obj = TestClass()

            val p1 = TestParam1("hello") to TestParam1::class.java
            val p2 = TestParam1("world") to TestParam1::class.java

            val result = obj.invokeMethod<String>("testMethod", p1, p2)
            assertEquals("hello world", result)
        }

        @Test
        fun `ensure invokeMethodInfer works lol 1-1`() {
            val obj = TestClass()

            val p1 = TestParam1("hello")
            val p2 = TestParam1("world")

            val result = obj.invokeMethodInfer<String>("testMethod", p1, p2)
            assertEquals("hello world", result)
        }
    }

    @Nested
    inner class InvokeMethodRethrowingTests {
        @Test
        fun `ensure invokeMethodRethrowing works lol 1-1`() {
            val obj = TestClass()

            val p1 = TestParam1("hello") to TestParam1::class.java
            val p2 = TestParam1("world") to TestParam1::class.java

            val result = obj.invokeMethod<String>("testMethod", p1, p2)
            assertEquals("hello world", result)
        }

        @Test
        fun `ensure invokeMethodRethrowingInfer works lol 1-1`() {
            val obj = TestClass()

            val p1 = TestParam1("hello")
            val p2 = TestParam1("world")

            val result = obj.invokeMethodInfer<String>("testMethod", p1, p2)
            assertEquals("hello world", result)
        }
    }

    @Nested
    inner class ExceptionTests {
        @Test
        fun `ensure invokeMethodRethrowing rethrows initial exception (before wrapping)`() {
            val obj = TestClass()

            assertThrows(TestException::class.java) {
                obj.invokeMethodRethrowing<String>("throwingMethod")
            }
        }

        @Test
        fun `ensure invokeMethodRethrowingInfer rethrows initial exception (before wrapping)`() {
            val obj = TestClass()

            assertThrows(TestException::class.java) {
                obj.invokeMethodRethrowingInfer<String>("throwingMethod")
            }
        }
    }

    data class TestParam1(val value: String)
    data class TestParam2(val valu3: Int)

    class TestException() : Exception()

    // uses java.lang.Object so as to give infer a fair chance, as such things like Kotlin's
    // and Java's Integers are different classes.
    inner class TestClass {
        fun testMethod(a: TestParam1, b: TestParam1): String {
            return "${a.value} ${b.value}"
        }

        fun testMethod(a: TestParam1, b: TestParam2): String {
            return "${a.value} ${b.valu3}"
        }

        fun throwingMethod(): String {
            throw TestException()
        }
    }
}
