/* ==================================================================   
 * Created [2006-12-28] by Jon.King 
 * ==================================================================  
 * TSS 
 * ================================================================== 
 * mailTo:jinpujun@hotmail.com
 * Copyright (c) Jon.King, 2012-2015 
 * ================================================================== 
 */

package com.jinhe.tss.cache.extension;

import com.jinhe.tss.cache.Cacheable;
import com.jinhe.tss.cache.ObjectPool;
import com.jinhe.tss.cache.PoolEvent;

/**
 * 缓存项要求可重复利用的，即缓存项的value对象实现了Reusable接口。 <br/>
 * 适合用于线程池，连接池，任务池等。 <br/>
 * 
 * 在该类中，缓存池中的对象由算法类的create()方法生成， <br/>
 * 在缓存池初始化以及check-out时对象不够的情况下创建新的对象。
 * 
 */
public class ReusablePool extends ObjectPool {
	
	/**
	 * 将元素从free池中取出，放入using池中，并返回该元素。 <br/>
	 * 
	 * 如果没有空置的元素可以获取，则一个新的元素将会创建出来，除非到了池的上限值。 <br/>
	 * 
	 * 如果一个空置的元素是错误的，它将被移出对象池，将会重新获取另外一个元素出来。
	 * 
	 * @return 
	 * 		池中的元素，如果没有可用对象返回null
	 * @exception PoolException
	 *      创建对象时出错则抛异常
	 */
	@Override
	protected Cacheable checkOut() {
		Cacheable item = null;
		if (getFree().size() > 0) {
			boolean valid;
			// 循环取，直到取出一个正确的或者free List为空为止
			do {
				item = remove();
				if ( valid = customizer.isValid(item) ) {
					break;
				} 
				else {
					customizer.destroy(item);
					log.info("将验证没通过的缓存项【" + item + "】销毁");
				}
			} while (getFree().size() > 0);

			if ( !valid ) {
				item = null;
			}
		}
		boolean isHited = (item != null); // 如果对象是从池中取出的，则命中率 hit＋＋

		// 如果free池中取不到，如果池中缓存项个数还没达到池的最大值，则新建一个
		if (item == null) {
			int maxSize = strategy.poolSize;
			if (size() < maxSize || maxSize == 0) {
				item = customizer.create();
				if ( !customizer.isValid(item) ) {
					String errorMsg = getName() + "池【" + getName() + "】没能创建一个新的有效的缓存项。";
					log.debug(errorMsg);
					throw new RuntimeException(errorMsg);
				}
			}
		}
		
		// 如果一个有效的缓存项已经得到或者创建出来，将它放入using集合中
		if (item != null) {
			item.addHit();
			item.updateAccessed();

			getUsing().put(item.getKey(), item);

			addRequests();
			if (isHited) {
				addHits();
			}

			firePoolEvent(PoolEvent.CHECKOUT);
		}
		
		log.debug("Check out end. result: " + (item == null ? " 无对象返回" : item.getKey()) 
		        + ".【" + getName() + " 的命中率 = " + getHitRate() + "%】");
		return item;
	}
}
