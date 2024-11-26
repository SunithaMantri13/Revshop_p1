package com.project.revshop.service;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

@Service
public class OTPService {
	private static final Integer EXPIRE_MINS = 5;
	
	private LoadingCache<String, Integer> otpCache;

	public OTPService() {
		otpCache = CacheBuilder.newBuilder().expireAfterWrite(EXPIRE_MINS, TimeUnit.MINUTES).build(new CacheLoader<String, Integer>(){
			public Integer load(String key) {
				return 0;
			}
		});
	}
	
	public int generateOTP(String key) {
		Random random = new Random();
		int otp = 100000 + random.nextInt(900000);
		otpCache.put(key, otp);
		return otp;
	}
	
	public int getOTP(String key) {
		try {
			return otpCache.get(key);
		} catch (ExecutionException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public void clearOTP(String key) {
		otpCache.invalidate(key);
	}
	
	public boolean validateOTP(String key, int enteredOTP) {
		int actualOTP = getOTP(key);
		if(actualOTP == enteredOTP) {
			return true;
		}
		else {
			return false;
		}
	}
}
