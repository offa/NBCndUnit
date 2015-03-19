
#include <CppUTest/TestHarness.h>

TEST_GROUP(ExampleSuite)
{

    void setup()
    {
        // Setup ...
        value = 3;
    }
    
    int value;
};

TEST(ExampleSuite, testSetup)
{
    CHECK_EQUAL(3, value);
}

TEST(ExampleSuite, testDouble)
{
    double d = 3.57;
    DOUBLES_EQUAL(3.56, d, 0.01);
}

TEST(ExampleSuite, testThatFails)
{
    // This test will fail
    int i = 3;
    CHECK_EQUAL(7, i);
}

