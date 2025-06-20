package com.sjdroid.screenshotblocker

import android.app.Activity
import org.junit.Test
import org.junit.Assert.*

/**
 * Unit tests for WindowSecurePolicy implementations
 */
class WindowSecurePolicyTest {
    
    // Test activities for testing
    class TestActivity : Activity()
    class LoginActivity : Activity()
    class PaymentActivity : Activity()
    class SecureActivity : Activity()
    class MainActivity : Activity()
    
    @SecureScreen
    class AnnotatedActivity : Activity()
    
    class NonAnnotatedActivity : Activity()
    
    @Test
    fun `AlwaysSecurePolicy should always return true`() {
        val policy = AlwaysSecurePolicy()
        val activity = TestActivity()
        assertTrue(policy.shouldSecure(activity))
    }
    
    @Test
    fun `NeverSecurePolicy should always return false`() {
        val policy = NeverSecurePolicy()
        val activity = TestActivity()
        assertFalse(policy.shouldSecure(activity))
    }
    
    @Test
    fun `ConditionalSecurePolicy should secure matching activity by full class name`() {
        val policy = ConditionalSecurePolicy(
            "com.sjdroid.screenshotblocker.WindowSecurePolicyTest\$SecureActivity"
        )
        
        val secureActivity = SecureActivity()
        val mainActivity = MainActivity()
        
        assertTrue(policy.shouldSecure(secureActivity))
        assertFalse(policy.shouldSecure(mainActivity))
    }
    
    @Test
    fun `ConditionalSecurePolicy should secure matching activity by simple class name`() {
        val policy = ConditionalSecurePolicy("SecureActivity")
        
        val secureActivity = SecureActivity()
        val mainActivity = MainActivity()
        
        assertTrue(policy.shouldSecure(secureActivity))
        assertFalse(policy.shouldSecure(mainActivity))
    }
    
    @Test
    fun `ConditionalSecurePolicy with multiple activities should work correctly`() {
        val policy = ConditionalSecurePolicy("LoginActivity", "PaymentActivity", "SecureActivity")
        
        // Test matching activities
        assertTrue(policy.shouldSecure(LoginActivity()))
        assertTrue(policy.shouldSecure(PaymentActivity()))
        assertTrue(policy.shouldSecure(SecureActivity()))
        
        // Test non-matching activity
        assertFalse(policy.shouldSecure(MainActivity()))
    }
    
    @Test
    fun `AnnotationBasedSecurePolicy should secure annotated activities`() {
        val policy = AnnotationBasedSecurePolicy()
        
        val annotatedActivity = AnnotatedActivity()
        val nonAnnotatedActivity = NonAnnotatedActivity()
        
        assertTrue(policy.shouldSecure(annotatedActivity))
        assertFalse(policy.shouldSecure(nonAnnotatedActivity))
    }
    
    @Test
    fun `AndSecurePolicy should require all policies to return true`() {
        val alwaysTrue = AlwaysSecurePolicy()
        val alwaysFalse = NeverSecurePolicy()
        val conditional = ConditionalSecurePolicy("TestActivity")
        
        val testActivity = TestActivity()
        val mainActivity = MainActivity()
        
        // All true policies
        val allTruePolicy = AndSecurePolicy(alwaysTrue, conditional)
        assertTrue(allTruePolicy.shouldSecure(testActivity))
        
        // Mix of true and false
        val mixedPolicy = AndSecurePolicy(alwaysTrue, alwaysFalse, conditional)
        assertFalse(mixedPolicy.shouldSecure(testActivity))
        
        // All false policies (conditional returns false for MainActivity)
        val allFalsePolicy = AndSecurePolicy(alwaysFalse, conditional)
        assertFalse(allFalsePolicy.shouldSecure(mainActivity))
    }
    
    @Test
    fun `OrSecurePolicy should require at least one policy to return true`() {
        val alwaysTrue = AlwaysSecurePolicy()
        val alwaysFalse = NeverSecurePolicy()
        val conditional = ConditionalSecurePolicy("TestActivity")
        
        val testActivity = TestActivity()
        val mainActivity = MainActivity()
        
        // At least one true policy
        val mixedPolicy = OrSecurePolicy(alwaysFalse, alwaysTrue, conditional)
        assertTrue(mixedPolicy.shouldSecure(mainActivity)) // alwaysTrue makes it true
        
        // All false policies
        val allFalsePolicy = OrSecurePolicy(alwaysFalse, conditional)
        assertFalse(allFalsePolicy.shouldSecure(mainActivity)) // Both false for MainActivity
        
        // Conditional becomes true
        val conditionalTruePolicy = OrSecurePolicy(alwaysFalse, conditional)
        assertTrue(conditionalTruePolicy.shouldSecure(testActivity)) // conditional true for TestActivity
    }
    
    @Test
    fun `AndSecurePolicy with vararg constructor should work correctly`() {
        val policy1 = AlwaysSecurePolicy()
        val policy2 = ConditionalSecurePolicy("TestActivity")
        
        val testActivity = TestActivity()
        
        val andPolicy = AndSecurePolicy(policy1, policy2)
        assertTrue(andPolicy.shouldSecure(testActivity))
    }
    
    @Test
    fun `OrSecurePolicy with vararg constructor should work correctly`() {
        val policy1 = NeverSecurePolicy()
        val policy2 = ConditionalSecurePolicy("TestActivity")
        
        val testActivity = TestActivity()
        
        val orPolicy = OrSecurePolicy(policy1, policy2)
        assertTrue(orPolicy.shouldSecure(testActivity))
    }
    
    @Test
    fun `Complex policy combinations should work correctly`() {
        val annotationPolicy = AnnotationBasedSecurePolicy()
        val conditionalPolicy = ConditionalSecurePolicy("PaymentActivity")
        val neverPolicy = NeverSecurePolicy()
        
        val paymentActivity = PaymentActivity()
        
        // OR(annotation, conditional) AND NOT(never)
        val complexPolicy = AndSecurePolicy(
            OrSecurePolicy(annotationPolicy, conditionalPolicy),
            neverPolicy // This will always be false, so AND will be false
        )
        
        assertFalse(complexPolicy.shouldSecure(paymentActivity)) // AND with NeverSecurePolicy = false
        
        // OR(annotation, conditional) - without the AND
        val simpleOrPolicy = OrSecurePolicy(annotationPolicy, conditionalPolicy)
        assertTrue(simpleOrPolicy.shouldSecure(paymentActivity)) // PaymentActivity matches conditional
    }
} 