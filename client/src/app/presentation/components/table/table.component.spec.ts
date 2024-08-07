import {ComponentFixture, TestBed} from '@angular/core/testing';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {MatTableDataSource} from '@angular/material/table';
import {SimpleChange, SimpleChanges} from '@angular/core';
import {TableComponent} from './table.component';

describe('TableComponent', () => {
  let component: TableComponent;
  let fixture: ComponentFixture<TableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TableComponent],
      providers: [
        { provide: MatPaginator, useValue: {} },
        { provide: MatSort, useValue: {} }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(TableComponent);
    component = fixture.componentInstance;
    component.dataSource = new MatTableDataSource<any>([]);
    component.displayedColumns = ['column1', 'column2'];
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should set paginator and sort on ngOnChanges if dataSource is present', () => {
    component.paginator = jasmine.createSpyObj('MatPaginator', ['']);
    component.sort = jasmine.createSpyObj('MatSort', ['']);
    const changes: SimpleChanges = {
      dataSource: new SimpleChange(null, component.dataSource, true)
    };

    component.ngOnChanges(changes);

    expect(component.dataSource.paginator).toBe(component.paginator);
    expect(component.dataSource.sort).toBe(component.sort);
  });

  it('should set paginator and sort on ngAfterViewInit if dataSource is present', () => {
    component.paginator = jasmine.createSpyObj('MatPaginator', ['']);
    component.sort = jasmine.createSpyObj('MatSort', ['']);

    component.ngAfterViewInit();

    expect(component.dataSource.paginator).toBe(component.paginator);
    expect(component.dataSource.sort).toBe(component.sort);
  });

});

