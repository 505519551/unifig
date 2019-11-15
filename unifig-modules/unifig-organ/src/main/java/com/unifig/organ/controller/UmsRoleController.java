package com.unifig.organ.controller;

import com.unifig.organ.domain.CommonResult;
import com.unifig.organ.model.UmsPermission;
import com.unifig.organ.model.UmsRole;
import com.unifig.organ.service.UmsRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * 后台用户角色管理
 *    on 2018/9/30.
 */
@Controller
@Api(tags = "UmsRoleController", description = "后台用户角色管理")
@RequestMapping("/role")
@ApiIgnore
public class UmsRoleController {
	@Autowired
	private UmsRoleService roleService;

	@ApiOperation("添加角色")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@ResponseBody
	public Object create(@RequestBody UmsRole role) {
		int count = roleService.create(role);
		if (count > 0) {
			return new CommonResult().success(count);
		}
		return new CommonResult().failed();
	}

	@ApiOperation("修改角色")
	@RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
	@ResponseBody
	public Object update(@PathVariable Long id, @RequestBody UmsRole role) {
		int count = roleService.update(id, role);
		if (count > 0) {
			return new CommonResult().success(count);
		}
		return new CommonResult().failed();
	}

	@ApiOperation("批量删除角色")
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	@ResponseBody
	public Object delete(String ids) {
		String[] split = ids.split(",");
		for (String id : split) {
			int i = Integer.parseInt(id);
			if (i<=3){
				return new CommonResult().failed("系统默认角色禁止删除！");
			}
		}
		int count = roleService.delete(ids);
		if (count > 0) {
			return new CommonResult().success(count);
		}
		return new CommonResult().failed();
	}

	@ApiOperation("获取相应角色权限")
	@RequestMapping(value = "/permission/{roleId}", method = RequestMethod.GET)
	@ResponseBody
	public Object getPermissionList(@PathVariable Long roleId) {
		List<UmsPermission> permissionList = roleService.getPermissionList(roleId);
		return new CommonResult().success(permissionList);
	}

	@ApiOperation("修改角色权限")
	@RequestMapping(value = "/permission/update", method = RequestMethod.POST)
	@ResponseBody
	public Object updatePermission(@RequestParam Long roleId,
								   @RequestParam("permissionIds") List<Long> permissionIds) {
		int count = roleService.updatePermission(roleId, permissionIds);
		if (count > 0) {
			return new CommonResult().success(count);
		}
		return new CommonResult().failed();
	}

	@ApiOperation("获取所有角色")
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public Object list() {
		List<UmsRole> roleList = roleService.list();
		return new CommonResult().success(roleList);
	}

}
