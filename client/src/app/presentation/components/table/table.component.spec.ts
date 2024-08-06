import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { SimpleChange, SimpleChanges } from '@angular/core';
import { TableComponent } from './table.component';

describe('TableComponent', () => {
  let component: TableComponent;
  let fixture: ComponentFixture<TableComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TableComponent],
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

  it('should not set paginator and sort if dataSource is not present', () => {
    component.dataSource = new MatTableDataSource<any>([]);

    component.ngOnChanges({});
    component.ngAfterViewInit();

    expect(component.dataSource).toBeUndefined();
  });

  it('should call actionButton onClick when the button is clicked', () => {
    const element = { id: 1 };
    component.actionButton = { label: 'Action', onClick: jasmine.createSpy('onClick') };

    fixture.detectChanges();

    // Simula o clique no botão
    const button = fixture.nativeElement.querySelector('button');
    button.click();

    expect(component.actionButton.onClick).toHaveBeenCalledWith(element);
  });
});
