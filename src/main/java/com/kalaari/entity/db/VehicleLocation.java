package com.kalaari.entity.db;

@Table(name = "vehicle_location")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "deleted=false")
public class VehicleLocation extends BaseEntity<Long> {

    @Column(name = "vehicle_id")
    private Long vehicleId;

    @Column(name = "idleSince")
    private Date idleSince;

    @Column(name = "lat")
    private Double lat;

    @Column(name = "lng")
    private Double lng;

}