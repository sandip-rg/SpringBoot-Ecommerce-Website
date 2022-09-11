package com.ecommerce.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ecommerce.model.CustomUserDetail;
import com.ecommerce.model.User;
import com.ecommerce.repository.UserRepository;

@Service
public class CustomUserDetailService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<User> user = this.userRepository.findUserByEmail(email);
		user.orElseThrow(()->new UsernameNotFoundException("User Not found"));
		return user.map(CustomUserDetail::new).get();
	}

}
