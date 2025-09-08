package pe.edu.utp.dwi.HBSGool.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@AllArgsConstructor
public class TestController {
	@GetMapping("")
	@ResponseStatus(HttpStatus.OK)
	public String test() {
		return "OK";
	}
}
