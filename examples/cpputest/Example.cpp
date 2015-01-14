
#include <CppUTest/TestHarness.h>

TEST_GROUP(Example)
{
};


TEST(Example, testThatWorks)
{
    int i = 3;
    CHECK_EQUAL(3, i);
}

TEST(Example, testThatWorks2)
{
    double d = 3.57;
    
    DOUBLES_EQUAL(3.56, d, 0.01);
}

TEST(Example, testThatTakesSomeTime)
{
    unsigned long value = 0;
    
    for( unsigned long i=0; i<10000000; i++ )
    {
        value += i;
    }
    
    CHECK_TRUE(value != 0UL);
}

/*
 * This test will fail!
 */
TEST(Example, testThatFails)
{
    int i = 99979;
    
    CHECK_EQUAL(99999, i);
}
