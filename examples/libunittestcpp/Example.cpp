
#include <libunittest/all.hpp>

using namespace unittest::assertions;

TEST(testThatWorks)
{
    int i = 3;
    
    assert_equal(3, i, SPOT);
}

TEST(testThatWorks2)
{
    double d = 3.57;
    
    assert_approx_equal(3.56, d, 0.01, SPOT);
}

TEST(testThatTakesSomeTime)
{
    unsigned long value = 0UL;
    
    for( unsigned long i=0UL; i<10000000; i++ )
    {
        value += i;
    }
    
    assert_true(value != 0UL, SPOT);
}

/*
 * This test will fail!
 */
TEST(testThatFails)
{
    int i = 99979;
    
    assert_equal(99999, i, SPOT);
}

