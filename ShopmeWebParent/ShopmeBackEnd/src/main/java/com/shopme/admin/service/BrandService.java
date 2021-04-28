package com.shopme.admin.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.admin.error.BrandNotFoundException;
import com.shopme.admin.repository.BrandRepository;
import com.shopme.admin.service.impl.IBrandService;
import com.shopme.common.entity.Brand;

@Service
public class BrandService implements IBrandService{

	@Autowired
	private BrandRepository repo;
	
	@Override
	public List<Brand> listAll() {
		// TODO Auto-generated method stub
		return (List<Brand>) repo.findAll();
	}

	@Override
	public Brand save(Brand brand) {
		return repo.save(brand);
	}

	@Override
	public Brand get(Integer id) throws BrandNotFoundException {
		try {
			return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new BrandNotFoundException("Could not find any brand with ID " + id);
		}
	}

	@Override
	public void delete(Integer id) throws BrandNotFoundException {
		Long countById = repo.countById(id);

		if (countById == null || countById == 0) {
			throw new BrandNotFoundException("Could not find any brand with ID " + id);			
		}

		repo.deleteById(id);
	}
}
