package cn.neyzoter.aiot.iov.biz.service.kafka.util;

/**
 * Kafka partition allocator
 * @author Neyzoter Song
 * @date 2020-2-6
 */
public class PartitionAllocator {
    /**
     * allocator by remainder
     * @param id
     * @param max_partition
     * @return
     */
    public static int allocateByRemainder (int id , int max_partition) throws IllPartitionException, IllIdException{
        if (max_partition <= 0) {
            throw new IllPartitionException();
        }
        if (id < 0) {
            throw new IllIdException();
        }
        return (id % max_partition);
    }
}

/**
 * illegal partition exception
 * @author Neyzoter Song
 * @date 2020-2-6
 */
class IllPartitionException extends Exception {
    IllPartitionException () {
        super("Illegal Partition Exception");
    }
}

/**
 * illegal id exception
 * @author Neyzoter Song
 * @date 2020-2-6
 */
class IllIdException extends Exception {
    IllIdException () {
        super("Illegal ID Exception");
    }
}