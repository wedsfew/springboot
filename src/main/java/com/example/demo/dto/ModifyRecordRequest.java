package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

/**
 * DNSPod修改记录请求DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DNSPod修改记录请求")
public class ModifyRecordRequest {

    @Schema(description = "域名", example = "example.com")
    @NotBlank(message = "域名不能为空")
    private String domain;

    @Schema(description = "记录类型", example = "A")
    @NotBlank(message = "记录类型不能为空")
    private String recordType;

    @Schema(description = "记录线路", example = "默认")
    @NotBlank(message = "记录线路不能为空")
    private String recordLine;

    @Schema(description = "记录值", example = "192.168.1.1")
    @NotBlank(message = "记录值不能为空")
    private String value;

    @Schema(description = "记录ID", example = "123456")
    @NotNull(message = "记录ID不能为空")
    private Long recordId;

    @Schema(description = "域名ID（可选）", example = "123")
    private Long domainId;

    @Schema(description = "主机记录", example = "www")
    private String subDomain;

    @Schema(description = "线路ID（可选）", example = "10=1")
    private String recordLineId;

    @Schema(description = "MX优先级（MX记录时必填）", example = "10")
    @Min(value = 1, message = "MX优先级最小值为1")
    @Max(value = 65535, message = "MX优先级最大值为65535")
    private Integer mx;

    @Schema(description = "TTL值", example = "600")
    @Min(value = 1, message = "TTL最小值为1")
    @Max(value = 604800, message = "TTL最大值为604800")
    private Integer ttl;

    @Schema(description = "权重信息", example = "20")
    @Min(value = 0, message = "权重最小值为0")
    @Max(value = 100, message = "权重最大值为100")
    private Integer weight;

    @Schema(description = "记录状态", example = "ENABLE")
    private String status;

    @Schema(description = "记录备注", example = "这是备注")
    private String remark;

    @Schema(description = "DNSSEC冲突模式", example = "force")
    private String dnssecConflictMode;
}