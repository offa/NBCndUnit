
#include <libunittest/all.hpp>

using namespace unittest::assertions;

struct ExampleSuiteFixture
{

    ExampleSuiteFixture()
    {
        // Setup ...
        value = 3;
    }

    int value;
};

COLLECTION(ExampleSuite)
{

    TEST_FIXTURE(ExampleSuiteFixture, testSetup)
    {
        assert_equal(3, value, SPOT);
    }
    
    TEST_FIXTURE(ExampleSuiteFixture, testDouble)
    {
        double d = 3.57;
        assert_approx_equal(3.56, d, 0.01, SPOT);
    }
    
    TEST_FIXTURE(ExampleSuiteFixture, testThatFails)
    {
        // This test will fail
        int i = 3;
        assert_equal(7, i, SPOT);
    }

}

