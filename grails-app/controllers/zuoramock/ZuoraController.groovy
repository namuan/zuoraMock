package zuoramock

import org.apache.commons.codec.binary.Base64

import java.security.MessageDigest

class ZuoraController {

    String url = "https://join.staging-thesun.co.uk/verify?"

    // https://apisandbox.zuora.com/apps/PublicHostedPaymentMethodPage.do?
    // method=submitPage&
    // id=2c92c0f93fa14376013fc056180c0cea&
    // tenantId=10578&
    // timestamp=1373909816280&
    // token=ngndv0X3J0RfUYjOA9Gx3OPIfyl9Zewd&
    // signature=ODdjZGQzZmE3NjNlNDE2NGFhODA1ZmRmZDJhMDVjNjU%3D&
    // field_creditCardType=Visa&
    // field_creditCardNumber=4111111111111111&
    // field_creditCardExpirationMonth=5&
    // field_creditCardExpirationYear=2020&
    // field_cardSecurityCode=123&
    // field_creditCardHolderName=Customer%20Api&field_creditCardCountry=gbr&field_creditCardAddress1=4%20Squarey%20Street&
    // field_creditCardAddress2=&field_creditCardCity=London&
    // field_creditCardPostalCode=SW17%200AB&field_passthrough1=https://join.staging-thesun.co.uk/&field_passthrough2=1

    def index() {

        String id = params.id
        String timestamp = params.timestamp
        String tenantId = params.tenantId
        String token = params.token
        String signature = generateSignature(id, tenantId, timestamp, token, getZuoraApiSecurityKey())

        String refId = "2c92c0f83fd52d73013fe35b40c32e65"

        redirect(url: "${url}id=${id}&tenantId=${tenantId}&timestamp=${timestamp}&token=${token}&responseSignature=${signature}&success=true&refId=${refId}&signature=${signature}&field_passthrough1=${params.field_passthrough1}&field_passthrough2=${params.field_passthrough2}")

    }

    def getZuoraApiSecurityKey() {
        "e4-XrytrNKcuD9BBl3ZXv3aoksrPMfslVTiFS_lItgg="
    }

    def generateSignature(id, tenantId, timestamp, token, apiSecurityKey) {
        def queryString = "id=${id}&tenantId=${tenantId}&timestamp=${timestamp}&token=${token}${apiSecurityKey}"
        def queryStringInUTF8Bytes = queryString.getBytes('UTF-8')

        MessageDigest md5Digester = MessageDigest.getInstance('MD5')
        def hashedQueryString = md5Digester.digest(queryStringInUTF8Bytes)

        //convert to hex
        String hashedStringInHex = hashedQueryString.encodeHex()

        //encode to Base64
        String hashedQueryStringBase64ed = new String(Base64.encodeBase64(hashedStringInHex.getBytes()))

        //url safe the signature                                                                         s
        hashedQueryStringBase64ed = hashedQueryStringBase64ed.replace('+', '-')
        hashedQueryStringBase64ed = hashedQueryStringBase64ed.replace('/', '_')

        return hashedQueryStringBase64ed
    }
}
