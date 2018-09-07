package com.app.controller;

import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.model.Vendor;
import com.app.service.IVendorService;
import com.app.util.VendorUtil;
import com.sun.istack.internal.logging.Logger;

@Controller
public class VendorController {
	private static Logger log=Logger.getLogger(VendorController.class);	
	@Autowired
	private IVendorService service;
	@Autowired
	private VendorUtil util;
	@Autowired
	private ServletContext context;
	//1.Register page:
	@RequestMapping("/regVen")
	public String showPage(){
		log.info("Entered into showReg ...");
		return "VendorReg";
	}
	
	//2.To save the Vendor:
	@RequestMapping(value="/insertVen",method=RequestMethod.POST)
	public String saveVendor(@ModelAttribute("vendor")Vendor ven,ModelMap map){
		log.info("Entered into saveVendor...");
		int vid=service.saveVendor(ven);
		
		map.addAttribute("ven", "Vendor Saved with: "+vid);
		return "VendorReg";
	}
	
	//3.To get data from DB: View All vendor details.
	@RequestMapping("/viewAllVendors")
		public String showAllVendors(ModelMap map){
			List<Vendor> vens=service.getAllVendors();
			map.addAttribute("vendors", vens);
			return "VendorData";
		}
	//4.To delete the Vendor:
	@RequestMapping("/deleteVen")
	public String deleteVendor(@RequestParam("venId")int vid){
		service.deleteVendor(vid);
		
		return"redirect:viewAllVendors";
	}
	//5.To show edit page for the Vendor:
	@RequestMapping("/editVen")
	public String editData(@RequestParam("venId")int vid, ModelMap map){
		Vendor ven=service.getVendorById(vid);
		map.addAttribute("ven", ven);
		return "VendorDataEdit";
	}
	
	//6.Update Vendor object:
	@RequestMapping(value="/updateVen",method=RequestMethod.POST)
	public String updateVendor(@ModelAttribute("vendor")Vendor ven){
		service.updateVendor(ven);
		return "redirect:viewAllVendors";
	}
	//7.Excel Export:
	@RequestMapping("/venExcelExport")
	public String showExcelFormat(ModelMap map){
		List<Vendor> vens=service.getAllVendors();
		map.addAttribute("vens", vens);
		return "venExcelView";
	}
	
	//8. PDF Format view:
	@RequestMapping("/venPdfExport")
	public String showPdfFormat(ModelMap map){
		List<Vendor> vens=service.getAllVendors();
		map.addAttribute("vens", vens);
		return "venPdfView";
	}
	
	//9. Reports: Pie chart and Bar chart:
	@RequestMapping("/showVendorReports")
	public String generateReports(){
		List<Object[]> list=service.getLocationTypeCount();
		String path=context.getRealPath("/");
		util.generateBarChart(path, list);
		util.generatePieChart(path, list);
		return "VendorReports";
	}

}
