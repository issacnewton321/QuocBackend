package com.abc.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.abc.entity.Danhmuc;
import com.abc.entity.Khachhang;
import com.abc.entity.Nhanvien;
import com.abc.entity.Quyen;
import com.abc.entity.Sanpham;
import com.abc.entity.Taikhoan;
import com.abc.model.Dangky;
import com.abc.model.NhanVienDangKy;
import com.abc.repository.NhanvienRepository;
import com.abc.repository.TaikhoanRepository;

@RestController
@CrossOrigin
public class NhanvienController {
	@Autowired
	NhanvienRepository repo;
	
	@Autowired
	TaikhoanRepository taiKhoanRepo;
	
	@GetMapping("/nhanvien")
	public List<Nhanvien> getListNV(){
		return repo.findAll();
	}
	
	@GetMapping("/nhanvien/{username}")
	public Nhanvien getNhanvien(@PathVariable("username") String username) {
		return repo.getNhanvienByUsername(username); 
	}
	
	@PutMapping("/nhanvien")
	public ResponseEntity<String> updateNhanvien(@Validated @RequestBody Nhanvien nhanvien) {

		try {
			List<Nhanvien> listNV = repo.findAll();
			for(Nhanvien nv : listNV) {
				if(nv.getManv().equalsIgnoreCase(nhanvien.getManv())) {
					repo.save(nhanvien);
					return new ResponseEntity<String>("Successed !!!",HttpStatus.OK);
				}
			}
			return new ResponseEntity<String>("Failed !!!",HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return new ResponseEntity<String>("Failed !!!",HttpStatus.BAD_REQUEST);
	}
	
	@PostMapping("/nhanvien")
	public ResponseEntity<String> register(@Validated @RequestBody NhanVienDangKy dangky){
		try {
			Nhanvien kh = new Nhanvien();
			Taikhoan tk = new Taikhoan();
			
			kh.setHo(dangky.getHo());
			kh.setTen(dangky.getTen());
			kh.setGioitinh(dangky.getGioitinh());
			kh.setSdt(dangky.getSdt());
			kh.setEmail(dangky.getEmail());
			kh.setDiachi(dangky.getDiachi());
			String manv = "NV" +  System.currentTimeMillis() % 10000000;
			kh.setManv(manv);
			kh.setLuong(dangky.getLuong());
			
			
			tk.setUsername(dangky.getUsername());
			tk.setPassword(dangky.getPassword());
			tk.setQuyen(new Quyen(3));
			kh.setTaikhoan(tk);
			
			for(Taikhoan t : taiKhoanRepo.findAll()) {
				if(t.getUsername().equalsIgnoreCase(tk.getUsername())) {
					return new ResponseEntity<String>("failed !!!",HttpStatus.BAD_REQUEST);
				}
			}
			taiKhoanRepo.save(tk);
			repo.save(kh);
			
			return new ResponseEntity<String>("successed !!!",HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return new ResponseEntity<String>("failed !!!",HttpStatus.BAD_REQUEST);
	}
	
	@DeleteMapping("/nhanvien/{matk}")
	public ResponseEntity<Boolean> deleteNV(@PathVariable("matk") int matk){
		try {
			repo.deleteNhanvienByMatk(matk);
			taiKhoanRepo.deleteById(matk);
			return new ResponseEntity<Boolean>(true,HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<Boolean>(false,HttpStatus.BAD_REQUEST);
			
		}
	}
}
