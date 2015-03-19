
#include <gtest/gtest.h>

class ExampleSuite : public testing::Test
{
protected:

    void SetUp()
    {
        // Setup ...
        value = 3;
    }
    
    int value;

};

TEST_F(ExampleSuite, testSetup)
{
    EXPECT_EQ(3, value);
}

TEST_F(ExampleSuite, testDouble)
{
    double d = 3.57;
    EXPECT_NEAR(3.56, d, 0.01);
}

TEST_F(ExampleSuite, testThatFails)
{
    // This test will fail
    int i = 3;
    EXPECT_EQ(7, 3);
}

