package org.make.front.operations

import org.make.front.models._

trait StaticDataOfOperation {
  val data: OperationStaticData
}

object Operations {
  val featuredOperationSlug: String = "vff"

  val operationDesignList: Seq[OperationStaticData] = Seq(
    VFFOperationStaticData.data,
    VFFITOperationStaticData.data,
    VFFGBOperationStaticData.data,
    ClimatParisOperationStaticData.data,
    LPAEOperationStaticData.data,
    MVEOperationStaticData.data
  )
}
