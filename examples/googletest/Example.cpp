
#include <gtest/gtest.h>

TEST(Example, testThatWorks)
{
    int i = 3;

    EXPECT_EQ(3, i);
}

TEST(Example, testThatWorks2)
{
    double d = 3.57;
    
    EXPECT_DOUBLE_EQ(3.57, d);
}

TEST(Example, testThatTakesSomeTime)
{
    unsigned long value = 0UL;
    
    for( unsigned long i=0UL; i<10000000; i++ )
    {
        value += i;
    }
    
    EXPECT_TRUE(value != 0UL);
}

/*
 * This test will fail!
 */
TEST(Example, testThatFails)
{
    int i = 99979;
    
    EXPECT_EQ(99999, i);
}

