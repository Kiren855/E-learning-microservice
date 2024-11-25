package com.sunny.microservices.payment.service;


import com.sunny.microservices.payment.constant.VNPayParams;
import com.sunny.microservices.payment.constant.VnpIpnResponseConst;
import com.sunny.microservices.payment.dto.response.IpnResponse;
import com.sunny.microservices.payment.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
@Slf4j
@RequiredArgsConstructor
public class VNPayIpnHandler implements IpnHandler {
    private final VNPayService vnPayService;

    ///!!!!!!!!!!!!!!111private final BookingService bookingService;


    public IpnResponse process(Map<String, String> params) {
        if (!vnPayService.verifyIpn(params)) {
            return VnpIpnResponseConst.SIGNATURE_FAILED;
        }

        IpnResponse response;
        var txnRef = params.get(VNPayParams.TXN_REF);
        try {
            //var bookingId = Long.parseLong(txnRef);
            ///!!!!!!!!!!!!!!!11bookingService.markBooked(bookingId);
            response = VnpIpnResponseConst.SUCCESS;
        }
        catch (BusinessException e) {
            switch (e.getResponseCode()) {
                case BOOKING_NOT_FOUND -> response = VnpIpnResponseConst.ORDER_NOT_FOUND;
                default -> response = VnpIpnResponseConst.UNKNOWN_ERROR;
            }
        }
        catch (Exception e) {
            response = VnpIpnResponseConst.UNKNOWN_ERROR;
        }

        log.info("[VNPay Ipn] txnRef: {}, response: {}", txnRef, response);
        return response;
    }
}
