<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="*">
        <xsl:copy>
            <xsl:copy-of select="@*"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
	<xsl:template name="price" match="orders/order/orderItems/orderItem/item/price/text()">
		<xsl:value-of select="concat('$',.)"/>
	</xsl:template>
	<xsl:template name="extPrice" match="orders/order/orderItems/orderItem/extPrice/text()">
		<xsl:value-of select="concat('$',.)"/>
	</xsl:template>
</xsl:stylesheet>