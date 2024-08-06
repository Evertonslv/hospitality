import {AfterViewInit, Component, Input, OnChanges, SimpleChanges, ViewChild} from '@angular/core'
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderCellDef,
  MatHeaderRow,
  MatHeaderRowDef,
  MatRow,
  MatRowDef,
  MatTable,
  MatTableDataSource
} from "@angular/material/table"
import {MatPaginator, MatPaginatorModule} from "@angular/material/paginator"
import {NgForOf, UpperCasePipe} from "@angular/common"
import {MatSort, MatSortModule} from "@angular/material/sort"
import {MatButton} from "@angular/material/button"

@Component({
  selector: 'app-table',
  standalone: true,
  imports: [
    MatTable, MatHeaderCell, MatCell, MatHeaderRow,
    MatRow, MatPaginator, NgForOf, MatHeaderCellDef,
    MatCellDef, MatHeaderRowDef, MatRowDef, MatColumnDef,
    MatSort, UpperCasePipe, MatButton, MatPaginatorModule,
    MatSortModule,
  ],
  templateUrl: './table.component.html',
  styleUrl: './table.component.scss'
})
export class TableComponent implements AfterViewInit, OnChanges {
  @Input() actionButton: { label: string, onClick: (element: any) => void }
  @Input() dataSource: MatTableDataSource<any>
  @Input() displayedColumns: string[]

  @ViewChild(MatPaginator) paginator: MatPaginator
  @ViewChild(MatSort) sort: MatSort

  ngOnChanges(changes: SimpleChanges) {
    if (changes['dataSource'] && this.dataSource) {
      this.dataSource.paginator = this.paginator
      this.dataSource.sort = this.sort
    }
  }

  ngAfterViewInit() {
    if (this.dataSource) {
      this.dataSource.paginator = this.paginator
      this.dataSource.sort = this.sort
    }
  }
}
