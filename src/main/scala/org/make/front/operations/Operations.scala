package org.make.front.operations

import org.make.front.models._

trait DesignOperation {
  val designData: OperationDesignData
}

object Operations {
  val featuredOperationSlug: String = "vff"

  val operationDesignList: Seq[OperationDesignData] = Seq(
    VffFRDesignOperation.designData,
    VffITDesignOperation.designData,
    VffGBDesignOperation.designData,
    ClimatParisDesignOperation.designData,
    LpaeDesignOperation.designData,
    MveDesignOperation.designData
  )
}
