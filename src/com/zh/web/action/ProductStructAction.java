package com.zh.web.action;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zh.core.base.action.Action;
import com.zh.core.base.action.BaseAction;
import com.zh.core.model.Pager;
import com.zh.web.model.ProductStructModel;
import com.zh.web.model.bean.BomDetail;
import com.zh.web.model.bean.BomPrimary;
import com.zh.web.model.bean.BomSub;
import com.zh.web.service.ProductStructService;

public class ProductStructAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3637616705310900755L;


	private static Logger LOGGER = LoggerFactory.getLogger(ProductStructAction.class); 

	
	private ProductStructModel productStructModel = new ProductStructModel();
	
	@Autowired
	private ProductStructService productStructService;
	
	@Override
	public Object getModel() {
		return productStructModel;
	}

	@Override
	public String execute() throws Exception {
		LOGGER.debug("execute()");
		BomPrimary bomPrimary = this.productStructModel.getBomPrimary();
		Integer count = productStructService.countPrimary(bomPrimary);
		Pager page = this.productStructModel.getPageInfo();
		page.setTotalRow(count);
		List<BomPrimary> bomPrimaryList = productStructService.queryPrimaryList(bomPrimary, page);
		this.productStructModel.setBomPrimaryList(bomPrimaryList);
		return Action.SUCCESS;

	}

	/**
	 * 编辑获取
	 */
	public String editor() throws Exception {
		LOGGER.debug("editor()");
		Integer id = this.productStructModel.getId();
		
		if (null != id){
			//查询信息
			LOGGER.debug("editor ProductStruct id " + id );
			BomPrimary bomPrimary = this.productStructModel.getBomPrimary();
			bomPrimary.setId(Integer.valueOf(id));
			//查询结果
			BomPrimary result = productStructService.queryPrimary(bomPrimary);
			LOGGER.debug("query bomPrimary:{}", result);
			this.productStructModel.setBomPrimary(result);
		
			//产品结构
			BomDetail bomDetail = this.productStructModel.getBomDetail();
			bomDetail.setPrimaryId(id);
			LOGGER.debug("bomDetail: {}", bomDetail);
			//Pager page = this.productStructModel.getPageInfo();
			//Integer count = productStructService.countDetail(bomDetail);
			//page.setTotalRow(count);
			//List<BomDetail> bomDetailList = productStructService.queryDetailList(bomDetail, page);
			List<BomDetail> bomDetailList = productStructService.queryDetailList(bomDetail);
			this.productStructModel.setBomDetailList(bomDetailList);
	
			//替代料列表
			BomSub bomSub = this.productStructModel.getBomSub();
			bomSub.setPrimaryId(id);
			LOGGER.debug("bomSub: {}", bomSub);
			List<BomSub> bomSubList = productStructService.querySubList(bomSub);
			this.productStructModel.setBomSubList(bomSubList);
			
		}
		
		return Action.EDITOR;
	}
	
	/**
	 * 查看
	 */
	public String view() throws Exception {
		LOGGER.debug("view()");
		Integer id = this.productStructModel.getId();
		if (null != id){
			//查询信息
			LOGGER.debug("view ProductStruct id " + id );
			BomPrimary bomPrimary = this.productStructModel.getBomPrimary();
			bomPrimary.setId(Integer.valueOf(id));
			//查询结果
			BomPrimary result = productStructService.queryPrimary(bomPrimary);
			LOGGER.debug("query bomPrimary:{}", result);
			this.productStructModel.setBomPrimary(result);
			
			//产品结构
			BomDetail bomDetail = this.productStructModel.getBomDetail();
			bomDetail.setPrimaryId(id);
			LOGGER.debug("bomDetail: {}", bomDetail);
//			Pager page = this.productStructModel.getPageInfo();
//			Integer count = productStructService.countDetail(bomDetail);
//			page.setTotalRow(count);
//			List<BomDetail> bomDetailList = productStructService.queryDetailList(bomDetail, page);
			List<BomDetail> bomDetailList = productStructService.queryDetailList(bomDetail);
			this.productStructModel.setBomDetailList(bomDetailList);
			
			//替代料列表
			BomSub bomSub = this.productStructModel.getBomSub();
			bomSub.setPrimaryId(id);
			LOGGER.debug("bomSub: {}", bomSub);
			List<BomSub> bomSubList = productStructService.querySubList(bomSub);
			this.productStructModel.setBomSubList(bomSubList);
		}
		return Action.VIEW;
	}

	/** 
	 * 保存结构主体
	 */
	public String save() throws Exception {
		LOGGER.debug("save()");
		BomPrimary bomPrimary = this.productStructModel.getBomPrimary();
		//头表的主键
		Integer id = bomPrimary.getId();
		//主键为空，则是插入，不为空，更新
		if (null != id && !"".equals(id)){
			bomPrimary.setId(id);
			productStructService.updatePrimary(bomPrimary);
			LOGGER.debug("update bomPrimary:{}", bomPrimary);
		}else{
			//新增
			productStructService.insertPrimary(bomPrimary);
			LOGGER.debug("add bomPrimary:{}", bomPrimary);
		}
		this.productStructModel.setId(bomPrimary.getId());
		return Action.EDITOR_SUCCESS;
	}
	
	/** 
	 * 保存结构明细
	 */
	public String saveDetail() throws Exception {
		LOGGER.debug("saveDetail()");
		BomDetail bomDetail = this.productStructModel.getBomDetail();
		//明细表的主键
		Integer id = bomDetail.getId();
		//主键为空，则是插入，不为空，更新
		if (null != id && !"".equals(id)){
			bomDetail.setId(id);
			productStructService.updateDetail(bomDetail);
			LOGGER.debug("update bomDetail:{}", bomDetail);
		}else{
			//新增
			productStructService.insertDetail(bomDetail);
			LOGGER.debug("add bomDetail:{}", bomDetail);
		}
		this.productStructModel.setId(bomDetail.getPrimaryId());
		return Action.EDITOR_SAVE;
	}
	
	/** 
	 * 删除结构明细记录
	 */
	public String deleteDetail() throws Exception {
		LOGGER.debug("deleteDetail()");
		BomDetail bomDetail = this.productStructModel.getBomDetail();
		//主表的id
		int primaryId = bomDetail.getPrimaryId();
		//明细表的主键
		Integer id = bomDetail.getId();
		if (null != id && !"".equals(id)){
			bomDetail.setId(id);
			productStructService.deleteDetail(bomDetail);
			LOGGER.debug("deleteDetail() bomDetail:{}", bomDetail);
		}else{
			LOGGER.debug("deleteDetail fail id is null");
		}
		this.productStructModel.setId(primaryId);
		return Action.EDITOR_SAVE;
	}
	
	/** 
	 * 保存主料替代料
	 */
	public String saveSub() throws Exception {
		LOGGER.debug("saveSub()");
		BomSub bomSub = this.productStructModel.getBomSub();
		//明细表的主键
		Integer id = bomSub.getId();
		//主键为空，则是插入，不为空，更新
		if (null != id && !"".equals(id)){
			bomSub.setId(id);
			productStructService.updateSub(bomSub);
			LOGGER.debug("update bomSub:{}", bomSub);
		}else{
			//新增
			productStructService.insertSub(bomSub);
			LOGGER.debug("add bomSub:{}", bomSub);
		}
		this.productStructModel.setId(bomSub.getPrimaryId());
		return Action.EDITOR_SAVE;
	}
	
	/** 
	 * 删除主料替代料
	 */
	public String deleteSub() throws Exception {
		LOGGER.debug("deleteSub()");
		BomSub bomSub = this.productStructModel.getBomSub();
		//主表的id
		int primaryId = bomSub.getPrimaryId();
		//明细表的主键
		Integer id = bomSub.getId();
		if (null != id && !"".equals(id)){
			bomSub.setId(id);
			productStructService.deleteSub(bomSub);
			LOGGER.debug("deleteSub() bomSub:{}", bomSub);
		}else{
			LOGGER.debug("deleteSub fail id is null");
		}
		this.productStructModel.setId(primaryId);
		return Action.EDITOR_SAVE;
	}

}